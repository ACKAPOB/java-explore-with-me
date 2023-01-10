package ru.practicum.ewm.user.service;


import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids);

    void deleteUser(Long userId);

    User getUser(Long userId);
}
