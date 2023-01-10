package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequestByRequester(Long userId, Long reqId);

    List<RequestDto> getForUserHisRequests(Long userId);
}
