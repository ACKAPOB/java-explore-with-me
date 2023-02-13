package ru.practicum.explore.request.service;

import ru.practicum.explore.request.dto.RequestDto;

import java.util.List;

public interface PrivateRequestService {
    List<RequestDto> get(Long userId);

    RequestDto post(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
