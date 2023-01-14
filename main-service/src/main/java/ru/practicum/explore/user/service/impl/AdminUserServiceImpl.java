package ru.practicum.explore.user.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.user.service.AdminUserService;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    AdminUserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        return userRepository
                .findAllByIdOrderByIdDesc(ids, PageRequest.of(from / size, size))
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(NewUserRequest userRequest) {
        User user = userMapper.toUser(userRequest);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        userRepository.deleteById(userId);
    }

}
