package ru.practicum.explore.user.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exception.ConflictException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.user.service.AdminUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        log.info("Получение информации о пользователях AdminUserServiceImpl.getAllUsers");
        return userRepository
                .findAllByIdOrderByIdDesc(ids, PageRequest.of(from / size, size))
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto postUser(NewUserRequest userRequest) {
        log.info("Добавление нового пользователя newUserRequest = {} AdminUserServiceImpl.postUser", userRequest);
        try {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
        } catch (Exception e)  {
            throw new ConflictException(String.format("Ошибка имя пользователя уже сущетсвует postUser name " +
                    "= {}", userRequest.getName()));
        }
    }

    @Override
    public void deleteUser(Long userId) {
        log.info("Удаление пользователя userId = {} AdminUserServiceImpl.deleteUser", userId);
        userRepository
                .delete(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден " +
                        "deleteUser id = %s", userId))));
    }

}
