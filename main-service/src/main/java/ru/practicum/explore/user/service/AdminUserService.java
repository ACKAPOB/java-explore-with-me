package ru.practicum.explore.user.service;

import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;

import java.util.List;

public interface AdminUserService {

    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    UserDto post(NewUserRequest userRequest);

    void delete(Long userId);
}
