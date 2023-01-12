package ru.practicum.explore.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto обновления пользователя
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    private String name;
    private String email;
}
