package ru.practicum.explore.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.mapper.model.Request;

import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus().toString())
                .build();
    }
}
