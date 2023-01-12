package ru.practicum.explore.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.clients.stat.StatClient;
import ru.practicum.explore.clients.EndpointHit;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.exception.ForbiddenRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private ParticipationRequestRepository participationRequestRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final CompilationMapper compilationMapper;

    private final CompilationRepository compilationRepository;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            ParticipationRequestRepository participationRequestRepository, EventMapper eventMapper,
                            StatClient statClient, CategoryRepository categoryRepository, CategoryMapper categoryMapper, UserRepository userRepository, UserMapper userMapper, CompilationMapper compilationMapper, CompilationRepository compilationRepository) {
        this.eventRepository = eventRepository;
        this.participationRequestRepository = participationRequestRepository;
        this.eventMapper = eventMapper;
        this.statClient = statClient;
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.compilationMapper = compilationMapper;
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Collection<EventShortDto> getAllEvent(Map<String, Object> parameters) {
        log.info("Получение событий с возможностью фильтрации EventServiceImpl.getAllEvent {}", parameters);
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));

        String text = (String) parameters.get("text");

        List<Long> categories = (List<Long>) parameters.get("categories");

        Boolean paid = (Boolean) parameters.get("paid");

        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Collection<Event> listEvent =
                eventRepository.getAllEventsByParameters(text, categories, paid, rangeStart, rangeEnd, pageable);

        Collection<EventShortDto> listEventShort = listEvent.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        if (parameters.get("sort").equals("EVENT_DATE")) {
            listEventShort.stream().sorted(Comparator.comparing(EventShortDto::getEventDate));
        }
        if (parameters.get("sort").equals("VIEWS")) {
            listEventShort.stream().sorted(Comparator.comparing(EventShortDto::getViews));
        }
        return listEventShort;
    }

    @Override
    public Optional<EventFullDto> getEvent(Long eventId) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "EventServiceImpl.getEvent id={}", eventId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(event);
        return Optional.of(eventFullDto);
    }

    @Override
    public void saveInStatService(HttpServletRequest request) {
        log.info("Отпарвление данных в стат сервер " +
                "EventServiceImpl.getEvent id={}", request);
        EndpointHit endpointHit = EndpointHit.builder()
                .app("ewm-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        statClient.save(endpointHit);
    }

    @Override
    public Collection<EventFullDto> getAllEvents(Map<String, Object> parameters) {
        Pageable pageable = PageRequest.of((Integer) parameters.get("from") / (Integer) parameters.get("size"),
                (Integer) parameters.get("size"));
        List<Long> users = (List<Long>) parameters.get("users");
        List<Status> states = (List<Status>) parameters.get("states");
        List<Long> catIds = (List<Long>) parameters.get("categories");
        LocalDateTime rangeStart = LocalDateTime.parse((String) parameters.get("rangeStart"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime rangeEnd = LocalDateTime.parse((String) parameters.get("rangeEnd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Collection<EventFullDto> fullDtoCollection =
                eventRepository.getAllEvents(users, states, catIds, rangeStart, rangeEnd, pageable).stream()
                        .map(eventMapper::toEventFullDto)
                        .collect(Collectors.toList());
        return fullDtoCollection;
    }

    @Override
    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        LocalDateTime eventDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2))) {
            throw new ForbiddenRequestException("Bad date.");
        }
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        if (adminUpdateEventRequest.getCategory() != null) {
            if (!categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).isPresent()) {
                throw new ObjectNotFoundException("Category not found.");
            }
            Category category = categoryRepository.findById(Long.valueOf(adminUpdateEventRequest.getCategory())).get();
            event.setCategory(category);
            event.setEventDate(eventDate);
        }

        Optional.ofNullable(adminUpdateEventRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(adminUpdateEventRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(adminUpdateEventRequest.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(adminUpdateEventRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(adminUpdateEventRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(adminUpdateEventRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(adminUpdateEventRequest.getTitle()).ifPresent(event::setTitle);

        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approvePublishEvent(Long eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        event.setState(Status.PUBLISHED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public EventFullDto approveRejectEvent(Long eventId) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        event.setState(Status.CANCELED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    public CategoryDto patchCategory(CategoryDto categoryDto) {
        if (categoryRepository.findFirstByName(categoryDto.getName()).isPresent()) {
            throw new ForbiddenRequestException(String.format("Bad name"));
        }
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s", categoryDto.getId())));
        categoryMapper.updateCategoryFromCategoryDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        Category category = categoryMapper.toCategory(newCategoryDto);
        CategoryDto categoryDto = categoryMapper.toCategoryDto(categoryRepository.save(category));
        return categoryDto;
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Category not found id = %s", catId)));
        categoryRepository.deleteById(catId);
    }

    @Override
    public Collection<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<UserDto> userDtoCollection = userRepository.findAllByIdOrderByIdDesc(ids, pageable).stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
        return userDtoCollection;
    }

    @Override
    public UserDto postUser(NewUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        UserDto userDto = userMapper.toUserDto(userRepository.save(user));
        return userDto;
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        userRepository.deleteById(userId);
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = events.stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return compilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        compilationRepository.deleteById(compId);
    }

    @Override
    public void deleteEventInCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        if (!compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventInCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        if (compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

}
