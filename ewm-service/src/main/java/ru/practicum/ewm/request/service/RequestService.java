package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequestByRequester(Long userId, Long reqId);

    List<RequestDto> getForUserHisRequests(Long userId);
}
