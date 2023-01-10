package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.RequestDto;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.model.Request;

@Component
public class RequestMapper {
    public static Request toRequest(RequestDto requestDto) {
        return Request.builder()
                .id(requestDto.getId())
                .eventId(requestDto.getEvent())
                .requester(requestDto.getRequester())
                .status(requestDto.getStatus())
                .created(requestDto.getCreated())
                .build();
    }

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .event(request.getEventId())
                .requester(request.getRequester())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
