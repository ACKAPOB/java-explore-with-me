package ru.practicum.explore.exception.errors.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.user.repository.UserRepository;

@Component
public class ObjectValidate {
    private CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository participationRequestRepository;

    @Autowired
    public ObjectValidate(CategoryRepository categoryRepository, CompilationRepository compilationRepository,
                          EventRepository eventRepository, UserRepository userRepository,
                          ParticipationRequestRepository participationRequestRepository) {
        this.categoryRepository = categoryRepository;
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.participationRequestRepository = participationRequestRepository;
    }

    public void validateUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new ObjectNotFoundException(String.format("User not found id=%s", userId));
        }
    }

    public void validateEvent(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Event not found id=%s", eventId));
        }
    }

    public void validateRequest(Long reqId) {
        if (!participationRequestRepository.findById(reqId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Request not found id=%s", reqId));
        }
    }

    public void validateCompilation(Long compId) {
        if (!compilationRepository.findById(compId).isPresent()) {
            throw new ObjectNotFoundException(String.format("Compilation not found id=%s", compId));
        }
    }
}
