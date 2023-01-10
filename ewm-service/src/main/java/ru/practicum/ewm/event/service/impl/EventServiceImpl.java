package ru.practicum.ewm.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.request.mapper.RequestMapper;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.category.service.CategoryService;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestState;
import ru.practicum.ewm.stats.EndpointHit;
import ru.practicum.ewm.stats.EventStatClient;
import ru.practicum.ewm.user.service.UserService;
import ru.practicum.ewm.stats.Stat;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.ewm.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.ewm.category.mapper.CategoryMapper.toCategoryFromCategoryDto;
import static ru.practicum.ewm.event.mapper.EventMapper.*;
import static ru.practicum.ewm.request.mapper.RequestMapper.toRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final WebClient webClient;
    private final ModelMapper mapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventStatClient eventStatClient;

    //////////////// EventsController ///////////////////
    @Override
    @Transactional // рабочий вариант но какой то стремный
    public List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                        String rangeStart, String rangeEnd, Integer from, Integer size) {
        log.info("Поиск событий EventServiceImpl.getEvents, users={}, states={}, categories={}, rangeStart={}, " +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        List<Event> events = eventRepository.findAllWithParameters(users, states, categories,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter),
                PageRequest.of(from / size, size, Sort.by("id").descending()));
        for (Event event : events) {
            eventFullDtos.add(toEventFullDto(event, userService.getUser(event.getInitiator()),
                    toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()))));
        }
        return eventFullDtos;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByAdmin(Long eventId, NewEventDto newEventDto) {
        log.info("Запрос на Редактирование события администратором EventController.updateEvent id {}, " +
                "updateEvent {}", eventId, newEventDto);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId)));
        return updateEventExtraction(
                newEventDto,
                event,
                userService.getUser(event.getInitiator()),
                toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory())));
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        log.info("Запрос на публикацию события  EventServiceImpl.publishEvent id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует, " +
                        "EventServiceImpl.publishEvent", eventId)));
        if (!event.getState().equals(EventState.PENDING))
            throw new ValidationException("Событие должно быть в статусе ожидания публикации.");
        if (event.getEventDate().plusHours(1).isBefore(LocalDateTime.now()))
            throw new ValidationException("Дата начала события должна быть не ранее чем за час от даты публикации.");
        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        event.setConfirmedRequests(0);
        eventRepository.save(event);
        return toEventFullDto(
                event,
                userService.getUser(event.getInitiator()),
                toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()))
        );
    }

    @Override
    @Transactional
    public EventFullDto denyEventByAdmin(Long eventId) {
        log.info("Запрос на публикацию события  EventServiceImpl.denyEventByAdmin eventId = {}", eventId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId)));
        if (event.getState().equals(EventState.PUBLISHED))
            throw new ValidationException("Событие уже опубликовано.");
        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        return toEventFullDto(
                event,
                userService.getUser(event.getInitiator()),
                toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory())));
    }

     ///////////// PublicEventController ///////////////////

    @Override
    @Transactional
    public List<EventShortDto> getSortedEvents(String text, List<Long> categories,
                                               Boolean paid, String rangeStart,
                                               String rangeEnd, Integer from,
                                               Integer size, HttpServletRequest request, EndpointHit endpointHit) {
        log.info("Получение событий с возможностью фильтрации EventServiceImpl.getSortedEvents text={}, " +
                        "categories={}, paid={}, rangeStart={}, rangeEnd={}, from={}, size={}, request = {}",
                text, categories, paid, rangeStart, rangeEnd, from, size, request);
        eventStatClient.sendHit(endpointHit);
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        List<Event> events = eventRepository.findAllWithMoreRequirements(text, categories, paid,
                LocalDateTime.parse(rangeStart, formatter), LocalDateTime.parse(rangeEnd, formatter),
                PageRequest.of(from / size, size, Sort.by("id").descending())
        );

        for (Event event : events) {
            EventShortDto eventShortDto = toEventShortDto(
                    event,
                    toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory())),
                    userService.getUser(event.getInitiator())
            );
            eventShortDtos.add(eventShortDto);
        }
        return eventShortDtos;
    }

    @Override
    @Transactional
    public EventFullDto getEventById(Long eventId, HttpServletRequest request, EndpointHit endpointHit) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "EventServiceImpl.getEventById, eventId = {}, request = {}", eventId, request);
        eventStatClient.sendHit(endpointHit);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId)));
        User user = userService.getUser(event.getInitiator());
        Category category = toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()));
        return toEventFullDto(event, user, category);
    }


     ///////////////////// PrivateEventController /////////////////////

    @Override
    @Transactional
    public List<EventFullDto> getEventsForInitiator(Long userId, Integer from, Integer size) {
        log.info("Получение событий, добавленных текущим пользователем EventServiceImpl " +
                "getEventsForInitiator userId={}", userId);
        List<EventFullDto> eventFullDtos = new ArrayList<>();
        List<Event> events = new ArrayList<>(eventRepository.findAllByInitiator(userId,
                PageRequest.of(from / size, size, Sort.by("id").descending())));
        for (Event event : events) {
            eventFullDtos.add(toEventFullDto(event, userService.getUser(event.getInitiator()), toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()))));
        }
        return eventFullDtos;
    }

    @Override
    @Transactional
    public EventFullDto updateEventByInitiator(Long userId, NewEventDto newEventDto) {
        log.info("Изменение события добавленного текущим пользователем EventServiceImpl.updateEventByInitiator " +
                "userId={}", userId);
        Event event = eventRepository.findAllByInitiator(userId, LocalDateTime.now().plusHours(2))
                .stream()
                .filter(event1 -> event1.getState().equals(EventState.CANCELED) ||
                        event1.getState().equals(EventState.PENDING))
                .findAny()
                .orElseThrow(() -> new NotFoundException("События по заданным параметрам нет"));
        event.setState(EventState.PENDING);
        return updateEventExtraction(
                newEventDto,
                event,
                userService.getUser(userId),
                toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()))
        );
    }

    @Override
    @Transactional
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        log.info("Добавление нового события EventServiceImpl.saveEvent userId={}, event={}", userId, newEventDto);
        if (newEventDto.getTitle() == null || newEventDto.getTitle().isBlank()) {
            throw new ValidationException("Событие составлено некорректно");
        }
        if (newEventDto.getAnnotation() == null || newEventDto.getAnnotation().isBlank()) {
            throw new ValidationException("Событие составлено некорректно");
        }
        if (LocalDateTime.parse(newEventDto.getEventDate(), formatter).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("Событие должно начаться не раньшем, чем через два часа от текущего времени");
        }
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
    @Transactional
    public EventFullDto getEventForUser(Long userId, Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем " +
                "EventServiceImpl.getEventForUser userId={}, eventId = {}", eventId, userId);
        return toEventFullDto(
                eventRepository.findByIdAndInitiator(userId, eventId),
                userService.getUser(userId),
                toCategoryFromCategoryDto(categoryService.getCategoryById(
                        eventRepository.findByIdAndInitiator(userId, eventId).getCategory())));
    }

    @Override
    @Transactional
    public EventFullDto denyEventByUser(Long userId, Long eventId) {
        log.info("Отмена события добавленного текущим пользователем EventServiceImpl.denyEventByInitiator  " +
                "event={}, userId={}", eventId, userId);
        Event event = eventRepository.findByIdAndInitiator(userId, eventId);
        if (event.getState() != EventState.PENDING)
            throw new ValidationException("Нельзя отменить событие в текущем статус.");
        event.setState(EventState.CANCELED);
        return toEventFullDto(
                eventRepository.save(event),
                userService.getUser(userId),
                toCategoryFromCategoryDto(categoryService.getCategoryById(event.getCategory()))
        );
    }

    @Override
    @Transactional
    public List<RequestDto> getInfoAboutRequestsForEventOwner(Long userId, Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя userId = {}, " +
                "eventId = {} EventServiceImpl.getInfoAboutRequestsForEventOwner", userId, eventId);
        return requestRepository.getInfoAboutRequestsForEventOwner(userId, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        log.info("Подтверждение чужой заявки на участие в событии текущего пользователя " +
                "userId = {}, eventId = {}, reqId = {} EventServiceImpl.confirmRequest", userId, eventId, reqId);
        Request request = requestRepository
                .findByInitiatorAndRequestAndEvent(reqId, userId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId)));
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0
                || (event.getParticipantLimit() - event.getConfirmedRequests()) != 0) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestState.REJECTED);
        }
        return toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public RequestDto rejectRequestByOwner(Long userId, Long eventId, Long reqId) {
        log.info("Отклонение чужой заявки на участие в событии текущего пользователя " +
                "userId = {}, eventId = {}, reqId = {}User EventServiceImpl.rejectRequest", userId, eventId, reqId);
        Request request = requestRepository
                .findByInitiatorAndRequestAndEvent(reqId, userId, eventId);
        request.setStatus(RequestState.REJECTED);
        return toRequestDto(requestRepository.save(request));
    }

    //////////////////////////////////////////////

    private void postStats(HttpServletRequest request, List<String> uriAddress) {
        log.info("Метод EventServiceImpl.postStats request = {}, uriAddress = {},", request, uriAddress);
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

    private EventFullDto updateEventExtraction(NewEventDto newEventDto, Event event, User user, Category category) {
        log.info("Метод EventServiceImpl.updateEventExtraction newEventDto = {}, event = {},", newEventDto, event);
        Optional.ofNullable(newEventDto.getTitle()).ifPresent(event::setTitle);
        Optional.ofNullable(newEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDto.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(newEventDto.getCategory()).ifPresent(event::setCategory);
        if (newEventDto.getEventDate() != null)
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter));
        eventRepository.save(event);
        return toEventFullDto(event, user, category);
    }
}
