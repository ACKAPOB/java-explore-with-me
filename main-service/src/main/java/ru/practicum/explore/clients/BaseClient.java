package ru.practicum.explore.clients;

import io.micrometer.core.lang.Nullable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

public class BaseClient {
    private final RestTemplate restTemplate;

    public BaseClient(String serverUrl) {
        restTemplate = new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    private static ResponseEntity<String> prepareGatewayResponse(ResponseEntity<String> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    protected ResponseEntity<String> get(String path, @Nullable Map<String, Object> parameters) {
        return makeAndSendRequest(HttpMethod.GET, path, parameters, null);
    }

    protected <T> ResponseEntity<String> post(String path, T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<String> post(String path, @Nullable Map<String, Object> parameters, T body) {
        return makeAndSendRequest(HttpMethod.POST, path, parameters, body);
    }

    private <T> ResponseEntity<String> makeAndSendRequest(HttpMethod method, String path, @Nullable Map<String, Object> parameters, @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders());

        ResponseEntity<String> statsServerResponse;
        try {
            if (parameters != null) {
                statsServerResponse = restTemplate.exchange(path, method, requestEntity, String.class, parameters);
            } else {
                statsServerResponse = restTemplate.exchange(path, method, requestEntity, String.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
        return prepareGatewayResponse(statsServerResponse);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return headers;
    }
}