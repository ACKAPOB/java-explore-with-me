package ru.practicum.ewm.service;


import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.model.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids);

    void deleteUser(Long userId);

    User getUser(Long userId);
}
