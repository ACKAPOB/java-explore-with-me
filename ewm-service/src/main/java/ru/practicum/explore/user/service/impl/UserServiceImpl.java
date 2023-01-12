package ru.practicum.explore.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.exception.errors.validate.ObjectValidate;
import ru.practicum.explore.comment.comment.CommentMapper;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.request.mapper.RequestMapper;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.exception.ForbiddenRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.request.mapper.model.ParticipationRequest;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.comment.comment.rep.CommentRepository;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.location.service.LocationService;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.request.model.StatusRequest;
import ru.practicum.explore.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final LocationService locationService;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectValidate objectValidate;

    @Autowired
    public UserServiceImpl(UserMapper userMapper, EventMapper eventMapper, RequestMapper requestMapper,
                           UserRepository userRepository, EventRepository eventRepository,
                           LocationService locationService,
                           ParticipationRequestRepository participationRequestRepository,
                           CategoryRepository categoryRepository,
                           ObjectValidate objectValidate) {
        this.userMapper = userMapper;
        this.eventMapper = eventMapper;
        this.requestMapper = requestMapper;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.locationService = locationService;
        this.participationRequestRepository = participationRequestRepository;
        this.categoryRepository = categoryRepository;
        this.objectValidate = objectValidate;
    }

    @Override
    public Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size) {
        objectValidate.validateUser(userId);
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<EventShortDto> listEventShort =
                eventRepository.findAllByInitiator_IdOrderById(userId, pageable).stream()
                        .map(eventMapper::toEventShortDto)
                        .collect(Collectors.toList());
        return listEventShort;
    }

    @Override
    public EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(updateEventRequest.getEventId());
        Event event = eventRepository.findById(updateEventRequest.getEventId()).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("No event initiator"));
        }
        if (!event.getEventDate().isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(String.format("Bad date."));
        }
        if (event.getState().equals(Status.PUBLISHED)) {
            throw new ForbiddenRequestException(String.format("Sorry, event status published."));
        }
        if (updateEventRequest.getCategory() != null) {
            if (!categoryRepository.findById(Long.valueOf(updateEventRequest.getCategory())).isPresent()) {
                throw new ObjectNotFoundException(String.format("Category not found."));
            }
            Category category = categoryRepository.findById(Long.valueOf(updateEventRequest.getCategory())).get();
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
        objectValidate.validateUser(userId);
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException(String.format("Bad date."));
        }
        if (!categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).isPresent()) {
            throw new ObjectNotFoundException(String.format("Category not found."));
        }
        User user = userRepository.findById(userId).get();
        Location location = locationService.save(newEventDto.getLocation());
        Category category = categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).get();
        Event event = eventMapper.toEvent(newEventDto, user, location, category, eventDate);
        event.setState(Status.PENDING);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventRepository.save(event));
        return eventFullDto;
    }

    @Override
    public EventFullDto getEventFull(Long userId, Long eventId) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("Sorry you no Event initiator"));
        }
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("Sorry you no Event initiator"));
        }
        if (event.getState().equals(Status.PUBLISHED) || event.getState().equals(Status.CANCELED)) {
            throw new ForbiddenRequestException(String.format("Sorry but event status should be pending"));
        }
        event.setState(Status.CANCELED);
        return eventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public Collection<ParticipationRequestDto> getRequestByUser(Long userId, Long eventId) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(eventId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("Sorry you no Event initiator"));
        }
        Collection<ParticipationRequestDto> listRequest =
                participationRequestRepository.findAllByEvent(eventId, userId).stream()
                        .map(requestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
        return listRequest;
    }

    @Override
    public ParticipationRequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(eventId);
        objectValidate.validateRequest(reqId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("Sorry you no Event initiator"));
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(eventId,
                StatusRequest.CONFIRMED);
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(limitParticipant)) {
            participationRequest.setStatus(StatusRequest.REJECTED);
        }
        participationRequest.setStatus(StatusRequest.CONFIRMED);
        return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public ParticipationRequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(eventId);
        objectValidate.validateRequest(reqId);
        Event event = eventRepository.findById(eventId).get();
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            return null;
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(reqId).get();
        participationRequest.setStatus(StatusRequest.REJECTED);
        return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }

    @Override
    public Collection<ParticipationRequestDto> getRequestsByUser(Long userId) {
        objectValidate.validateUser(userId);
        Collection<ParticipationRequestDto> listRequests =
                participationRequestRepository.findAllByRequester_IdOrderById(userId).stream()
                        .map(requestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
        return listRequests;
    }

    @Override
    public ParticipationRequestDto postRequestUser(Long userId, Long eventId) {
        objectValidate.validateUser(userId);
        objectValidate.validateEvent(eventId);
        if (Objects.equals(eventRepository.findById(eventId).get().getInitiator().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("Sorry you no Event initiator"));
        }
        if (eventRepository.findById(eventId).get().getState().equals(Status.PUBLISHED)) {
            User user = userRepository.findById(userId).get();
            Event event = eventRepository.findById(eventId).get();
            ParticipationRequest participationRequest = new ParticipationRequest().builder()
                    .created(LocalDateTime.now())
                    .event(event)
                    .requester(user)
                    .status(StatusRequest.PENDING)
                    .build();
            Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(eventId,
                    StatusRequest.CONFIRMED);
            if (!event.getRequestModeration()) {
                participationRequest.setStatus(StatusRequest.CONFIRMED);
            }
            if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), limitParticipant)) {
                participationRequest.setStatus(StatusRequest.REJECTED);
            }
            return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
        } else {
            throw new ForbiddenRequestException(String.format("Sorry you no Event no published"));
        }
    }

    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        objectValidate.validateUser(userId);
        objectValidate.validateRequest(requestId);
        if (!Objects.equals(participationRequestRepository.findById(requestId).get().getRequester().getId(), userId)) {
            throw new ForbiddenRequestException(String.format("Sorry you no Event initiator"));
        }
        ParticipationRequest participationRequest = participationRequestRepository.findById(requestId).get();
        participationRequest.setStatus(StatusRequest.CANCELED);
        return requestMapper.toParticipationRequestDto(participationRequestRepository.save(participationRequest));
    }
}
