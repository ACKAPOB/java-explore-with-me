package ru.practicum.explore.request.service;

import ru.practicum.explore.request.dto.RequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<RequestDto> getRequestsByUser(Long userId);

    RequestDto postRequestUser(Long userId, Long eventId);

    RequestDto cancelRequestByUser(Long userId, Long requestId);
}
