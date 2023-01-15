package ru.practicum.explore.controllers.privateController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.service.PrivateRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
public class PrivateUserRequestController {
    private final PrivateRequestService requestService;

    @Autowired
    public PrivateUserRequestController(PrivateRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<RequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях. userId = {} " +
                "PrivateUserRequestController.getRequestsByUser", userId);
        return requestService.getRequestsByUser(userId);
    }

    @PostMapping
    public RequestDto postRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии userId = {} and eventId = {} " +
                "PrivateUserRequestController.postRequestUser", userId, eventId);
        return requestService.postRequest(userId, eventId);

    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequestByUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Отмена своего запроса на участие в событии userId = {}, requestId = {} " +
                "PrivateUserRequestController.cancelRequestByUser", userId, requestId);
        return requestService.cancelRequestByUser(userId, requestId);
    }

}

