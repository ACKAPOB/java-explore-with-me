package ru.practicum.ewm.controller.user;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.request.service.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<RequestDto> getForUserHisRequests(@PathVariable(name = "userId") Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях userId = {}," +
                "RequestController.getForUserHisRequests", userId);
        return requestService.getForUserHisRequests(userId);
    }

    @PostMapping
    public RequestDto createRequest(
            @PathVariable Long userId,
            @RequestParam(name = "eventId", required = false) Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии userId = {}, " +
                "eventId = {}, RequestController.createRequest", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequestByRequester(@PathVariable(name = "userId") Long userId,
                                               @PathVariable(name = "requestId") Long reqId) {
        log.info("Отмена своего запроса на участие в событии RequestController.cancelRequestByRequester, " +
                "reqId={}, userId={}", reqId, userId);
        return requestService.cancelRequestByRequester(userId, reqId);
    }
}
