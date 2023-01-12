package ewmservice.request.service;

import ewmservice.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto saveRequest(Long userId, Long eventId);

    RequestDto cancelRequestByRequester(Long userId, Long reqId);

    List<RequestDto> getForUserHisRequests(Long userId);

    List<RequestDto> getInfoAboutRequestsForEventOwner(Long userId, Long eventId);

    RequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    RequestDto rejectRequestByOwner(Long userId, Long eventId, Long reqId);
}
