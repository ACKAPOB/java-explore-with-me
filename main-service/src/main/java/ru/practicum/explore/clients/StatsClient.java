package ru.practicum.explore.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.practicum.explore.event.model.Event;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StatsClient extends BaseClient {
    private final String prefixPost = "/hit";
    private final String prefixGet = "/stats?start={start}&end={end}&unique={unique}&uris={uris}";
    private final ObjectMapper om;
    @Value("Explore-With-Me")
    private String app;

    @Autowired
    public StatsClient(@Value("${stats.server-url}") String url, ObjectMapper om) {
        super(url);
        this.om = om;
    }

    public void save(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        post(prefixPost, new EndpointHit(app, uri, ip));
    }

    public List<ViewStats> getViews(List<Event> events, boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String[] uris = events.stream().map(this::createUriFromEvent).toArray(String[]::new);
        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(LocalDateTime::compareTo).orElseThrow();
        parameters.put("start", URLEncoder.encode(formatter.format(start), StandardCharsets.UTF_8));
        parameters.put("end", URLEncoder.encode(formatter.format(LocalDateTime.now()), StandardCharsets.UTF_8));
        parameters.put("unique", unique);
        parameters.put("uris", uris);
        try {
            return Arrays.asList(om.readValue(get(prefixGet, parameters).getBody(), ViewStats[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String createUriFromEvent(Event event) {
        return "/events/" + event.getId();
    }
}
