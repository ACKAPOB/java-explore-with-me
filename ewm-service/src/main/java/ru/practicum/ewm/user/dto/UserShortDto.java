package ru.practicum.ewm.user.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class UserShortDto {
    private Long id;
    @NotBlank
    private String name;
}
