package ru.practicum.explore.user.mapper;

import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.model.User;

/**
 * Интерфейс маппера пользователя
 */
public interface UserMapper {
    /*
    Метод маппера  для получения dto пользователя из модели
    */
    UserDto toUserDto(User user);

    /*
    Метод маппера  для добовления модели пользователя из dto
    */
    User toUser(NewUserRequest newUserRequest);

    /*
    Метод маппера  для получения краткой информации о пользователе
    */
    UserShortDto toUserShortDto(User user);
}
