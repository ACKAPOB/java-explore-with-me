package ru.practicum.explore.user.mapper;

import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.model.User;

public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(NewUserRequest newUserRequest);

    UserShortDto toUserShortDto(User user);
}
