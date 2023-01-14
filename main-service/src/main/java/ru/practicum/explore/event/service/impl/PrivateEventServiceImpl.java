package ru.practicum.explore.event.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.service.PrivatEventService;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.location.service.LocationService;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.request.mapper.model.Request;
import ru.practicum.explore.request.model.StatusRequest;
import ru.practicum.explore.request.repository.RequestRepository;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class PrivateEventServiceImpl implements PrivatEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationService locationService;
    private final RequestRepository requestRepository;

    public PrivateEventServiceImpl(EventRepository eventRepository, EventMapper eventMapper,
                                   CategoryRepository categoryRepository, UserRepository userRepository,
                                   LocationService locationService, RequestRepository requestRepository) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.requestRepository = requestRepository;
    }

    @Override
    public Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size) {
        log.info("Получение событий, добавленных текущим пользователем " +
                "PrivateEventServiceImpl.findAllEventsByUserId text = {}", userId);
        checkUser(userId);
        return eventRepository
                .findAllByInitiator_IdOrderById(userId, PageRequest.of(from / size, size)).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        log.info("Изменение события добавленного текущим пользователем userId = {} " +
                "PrivateEventServiceImpl.patchEventByUser", userId);
        checkUser(userId);
        Event event = eventRepository
                .findById(updateEventRequest.getEventId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        updateEventRequest.getEventId())));

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ErrorRequestException("No event initiator");
        }
        if (!event.getEventDate().isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ErrorRequestException("Bad date.");
        }
        if (event.getState().equals(Status.PUBLISHED)) {
            throw new ErrorRequestException("Sorry, event status published.");
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository
                    .findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s",
                            updateEventRequest.getCategory())));
            event.setCategory(category);
        }
        eventMapper.updateEventFromNewEventDto(updateEventRequest, event);
        if (event.getState().equals(Status.CANCELED))
            event.setState(Status.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        log.info("Добавление нового события userId = {} PrivateEventServiceImpl.postEvent", userId);
        checkUser(userId);
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ErrorRequestException("Bad date.");
        }
        Category category = categoryRepository
                .findById(newEventDto.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s",
                        newEventDto.getCategory())));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        Location location = locationService.save(newEventDto.getLocation());
        Event event = eventMapper.toEvent(newEventDto, user, location, category, eventDate);
        event.setState(Status.PENDING);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventFull(Long userId, Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем"  +
                " eventId = {} userId= {} PrivateEventServiceImpl.getEventFull", eventId, userId);
        checkUser(userId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ErrorRequestException("Sorry you no Event initiator");
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        log.info("Отмена события добавленного текущим пользователем eventId={} by userId={} " +
                "PrivateEventServiceImpl.cancelEvent", eventId, userId);
        checkUser(userId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ErrorRequestException("Sorry you no Event initiator");
        }
        if (event.getState().equals(Status.PUBLISHED) || event.getState().equals(Status.CANCELED)) {
            throw new ErrorRequestException("Sorry but event status should be pending");
        }
        event.setState(Status.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getRequestByUser(Long userId, Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя " +
                "PrivateEventServiceImpl.getRequestByUser userId = {}, eventId={}", userId, eventId);
        checkUser(userId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ErrorRequestException("Sorry you no Event initiator");
        }
        return requestRepository.findAllByEvent(eventId, userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId) {
        log.info("Подтверждение чужой заявки на участие в событии текущего пользователя reqId = {}, userId = {}, " +
                "eventId = {} PrivateEventServiceImpl.approveConfirmUserByEvent", reqId, userId, eventId);
        checkUser(userId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request not found id = %s",
                        reqId)));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ErrorRequestException("Ошибка Вы не инициатор");
        }
        Integer limitParticipant = requestRepository.countByEvent_IdAndStatus(eventId,
                StatusRequest.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(limitParticipant)) {
            request.setStatus(StatusRequest.REJECTED);
        }
        request.setStatus(StatusRequest.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId) {
        checkUser(userId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request not found id = %s",
                        reqId)));
        if (!Objects.equals(event.getInitiator().getId(), userId))
            return null;
        request.setStatus(StatusRequest.REJECTED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    public void checkUser(Long userId) { // много повторений кода в классе, убираем варнинги ))
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s " +
                        "PrivateEventServiceImpl", userId)));
    }

}
