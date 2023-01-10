package ru.practicum.ewm.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "name")
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank
    private String name;
}
