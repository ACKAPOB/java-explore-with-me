package ewmservice.event.service;

import ewmservice.category.model.Category;
import ewmservice.category.service.CategoryServiceImpl;
import ewmservice.event.dto.EventFullDto;
import ewmservice.event.dto.EventShortDto;
import ewmservice.event.dto.NewEventDto;
import ewmservice.event.model.Event;
import ewmservice.event.model.EventState;
import ewmservice.event.repository.EventRepository;
import ewmservice.exception.EntityNotFoundException;
import ewmservice.exception.ValidationException;
import ewmservice.stats.Stat;
import ewmservice.stats.ViewStats;
import ewmservice.user.model.User;
import ewmservice.user.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static ewmservice.category.mapper.CategoryMapper.toCategoryFromCategoryDto;
import static ewmservice.event.mapper.EventMapper.*;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;
    private final WebClient webClient;
    private final ModelMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public EventServiceImpl(EventRepository eventRepository,
                            UserServiceImpl userService,
                            CategoryServiceImpl categoryService, WebClient webClient, ModelMapper mapper) {
        this.eventRepository = eventRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.webClient = webClient;
        this.mapper = mapper;
    }

    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        newEventDto.setInitiator(userId);
        newEventDto.setState(EventState.PENDING);
        newEventDto.setCreatedOn(LocalDateTime.now());
        User user = userService.getUser(userId);
        Category category = toCategoryFromCategoryDto(
                categoryService.getCategoryById(newEventDto.getCategory()));
        Event event = eventRepository.save(toEvent(newEventDto));
        return toEventFullDto(event, user, category);
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId)));
        validateEventForPublish(event);
        event.setState(EventState.PUBLISHED);
        User user = userService.getUser(event.getInitiator());
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        EventFullDto eventFullDto = toEventFullDto(event, user, category);
        eventFullDto.setPublishedOn(LocalDateTime.now());
        eventFullDto.setConfirmedRequests(0);
        eventRepository.save(toEventFromFullDto(eventFullDto));
        return eventFullDto;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, NewEventDto newEventDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId)));
        User user = userService.getUser(event.getInitiator());
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        return updateEventExtraction(newEventDto, event, user, category);
    }

    @Override
    public EventFullDto updateEventByInitiator(Long userId, NewEventDto newEventDto) {
        Event event = eventRepository.findAllByInitiator(userId, LocalDateTime.now().plusHours(2)).stream()
                .filter(event1 -> event1.getState().equals(EventState.CANCELED) ||
                        event1.getState().equals(EventState.PENDING))
                .findAny().orElseThrow(() -> new EntityNotFoundException("События по заданным параметрам нет"));
        User user = userService.getUser(userId);
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        event.setState(EventState.PENDING);
        return updateEventExtraction(newEventDto, event, user, category);
    }


    @Override
    public EventFullDto denyEventByAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId)));
        validateEventForRejection(event);
        event.setState(EventState.CANCELED);
        User user = userService.getUser(event.getInitiator());
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        eventRepository.save(event);
        return toEventFullDto(event, user, category);
    }

    @Override
    public EventFullDto denyEventByInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator(userId, eventId);
        if (event.getState() != EventState.PENDING) {
            throw new ValidationException("Нельзя отменить событие в текущем статус.");
        }
        event.setState(EventState.CANCELED);
        User user = userService.getUser(userId);
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        return toEventFullDto(eventRepository.save(event), user, category);
    }

    @Override
    public List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states,
                                                List<Long> categories, String rangeStart,
                                                String rangeEnd, Integer from, Integer size) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        List<Event> events = eventRepository.findAllWithParameters(users, states, categories,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter), pageRequest);
        return getEventFullDtos(eventFullDtos, events);
    }

    @Override
    public List<EventShortDto> getEventsForUser(String text, List<Long> categories,
                                                Boolean paid, String rangeStart,
                                                String rangeEnd, Integer from,
                                                Integer size, HttpServletRequest request) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        List<Event> events = eventRepository.findAllWithMoreRequirements(text, categories, paid,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter),
                pageRequest);
        List<String> uriAddress = new ArrayList<>();
        for (Event event : events) {
            uriAddress.add(request.getRequestURI() + "/" + event.getId());
        }
        postStats(request, uriAddress);
        Mono<Object[]> responseViewStats = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", rangeStart.replace("T", " "))
                        .queryParam("end", rangeEnd.replace("T", " "))
                        .queryParam("uris", uriAddress)
                        .queryParam("unique", false)
                        .build())
                .retrieve()
                .bodyToMono(Object[].class);
        Object[] objects = responseViewStats.block();
        List<ViewStats> views = new ArrayList<>();
        if (objects != null) {
            views = Arrays.stream(objects)
                    .map(o -> mapper.map(o, ViewStats.class)).collect(Collectors.toList());
        }
        for (Event event : events) {
            User user = userService.getUser(event.getInitiator());
            Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
            EventShortDto eventShortDto = toEventShortDto(event, category, user);
            eventShortDto.setViews(views.stream()
                    .map(ViewStats::getHits)
                    .mapToLong(Long::longValue)
                    .sum());
            eventShortDtos.add(eventShortDto);
        }
        return eventShortDtos;
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", id)));
        User user = userService.getUser(event.getInitiator());
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        Stat stat = new Stat();
        stat.setApp("ewm-main-service");
        stat.setIp(request.getRemoteAddr());
        stat.setUri(request.getRequestURI());
        stat.setTimestamp(LocalDateTime.now());
        webClient.post()
                .uri("/hit")
                .body(Mono.just(stat), Stat.class)
                .exchangeToMono(rs -> Mono.just(rs.mutate()))
                .block();
        List<String> uriAddress = new ArrayList<>();
        uriAddress.add(request.getRequestURI());
        Mono<Object[]> responseViewStats = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("uris", uriAddress)
                        .queryParam("unique", false)
                        .build())
                .retrieve()
                .bodyToMono(Object[].class);
        Object[] objects = responseViewStats.block();
        List<ViewStats> views = new ArrayList<>();
        if (objects != null) {
            views = Arrays.stream(objects)
                    .map(o -> mapper.map(o, ViewStats.class)).collect(Collectors.toList());
        }
        EventFullDto eventFullDto = toEventFullDto(event, user, category);
        eventFullDto.setViews(views.get(0).getHits());
        return eventFullDto;
    }


    @Override
    public EventFullDto getEventForInitiator(Long userId, Long eventId) {
        Event event = eventRepository.findByIdAndInitiator(userId, eventId);
        User user = userService.getUser(userId);
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        return toEventFullDto(event, user, category);
    }

    @Override
    public List<EventFullDto> getEventsForInitiator(Long userId, Integer from, Integer size) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        List<Event> events = new ArrayList<>(eventRepository.findAllByInitiator(userId, pageRequest));
        return getEventFullDtos(eventFullDtos, events);
    }

    private void postStats(HttpServletRequest request, List<String> uriAddress) {
        Stat stat = new Stat();
        stat.setApp("ewm-main-service");
        stat.setIp(request.getRemoteAddr());
        stat.setUri(uriAddress.toString());
        stat.setTimestamp(LocalDateTime.now());
        webClient.post()
                .uri("/hit")
                .body(Mono.just(stat), Stat.class)
                .exchangeToMono(rs -> Mono.just(rs.mutate()))
                .block();
    }

    private List<EventFullDto> getEventFullDtos(List<EventFullDto> eventShortDtos, List<Event> events) {
        for (Event event : events) {
            User user = userService.getUser(event.getInitiator());
            Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
            EventFullDto eventFullDto = toEventFullDto(event, user, category);
            eventShortDtos.add(eventFullDto);
        }
        return eventShortDtos;
    }

    private EventFullDto updateEventExtraction(NewEventDto newEventDto, Event event, User user, Category category) {
        Optional.ofNullable(newEventDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(newEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(newEventDto.getCategory()).ifPresent(event::setCategory);
        if (newEventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        }
        eventRepository.save(event);
        return toEventFullDto(event, user, category);
    }

    private void validateEventForPublish(Event event) {
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ValidationException("Событие должно быть в статусе ожидания публикации.");
        }
        if (event.getEventDate().plusHours(1).isBefore(LocalDateTime.now())) {
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации.");
        }
    }

    private void validateEventForRejection(Event event) {
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Событие уже опубликовано.");
        }
    }
}
