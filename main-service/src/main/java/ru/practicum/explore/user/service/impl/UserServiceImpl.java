package ru.practicum.explore.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.request.mapper.model.Request;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.request.repository.RequestRepository;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.location.service.LocationService;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.request.model.StatusRequest;
import ru.practicum.explore.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
class UserServiceImpl implements UserService {
    private final EventMapper eventMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;

    UserServiceImpl(EventMapper eventMapper, UserRepository userRepository, EventRepository eventRepository,
                    LocationService locationService, RequestRepository requestRepository,
                    CategoryRepository categoryRepository) {
        this.eventMapper = eventMapper;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        return eventRepository
                .findAllByInitiator_IdOrderById(userId, PageRequest.of(from / size, size)).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
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
        if (event.getState().equals(Status.CANCELED)) {
            event.setState(Status.PENDING);
        }
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
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
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
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
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
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
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
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
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request not found id = %s",
                        reqId)));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ErrorRequestException("Sorry you no Event initiator");
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
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s",
                        eventId)));
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Request not found id = %s",
                        reqId)));
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            return null;
        }
        request.setStatus(StatusRequest.REJECTED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}
