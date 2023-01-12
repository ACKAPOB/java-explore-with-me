package ru.practicum.explore.controllers.privateController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
public class PrivateUserRequestController {
    private final UserService userService;

    @Autowired
    public PrivateUserRequestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях. userId = {} " +
                "PrivateUserRequestController.getRequestsByUser", userId);
        return userService.getRequestsByUser(userId);
    }

    @PostMapping
    public ParticipationRequestDto postRequestUser(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии userId = {} and eventId = {} " +
                "PrivateUserRequestController.postRequestUser", userId, eventId);
        return userService.postRequestUser(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("User patch requestId={} by eserId={}", userId, requestId);
        return userService.cancelRequestByUser(userId, requestId);
    }

}
