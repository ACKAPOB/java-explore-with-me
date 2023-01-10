package ru.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.ewm.model.RequestState;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class RequestDto {
    private Long id;
    private Long requester;
    private Long event;
    private RequestState status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
}
