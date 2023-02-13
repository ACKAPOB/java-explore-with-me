package ru.practicum.explore.user.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        log.info("Получение информации о пользователях AdminUserServiceImpl.getAll");

        return userRepository
                .findAllSorted(ids, PageRequest.of(from / size, size))
                .stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto post(NewUserRequest userRequest) {
        log.info("Добавление нового пользователя newUserRequest = {} AdminUserServiceImpl.post", userRequest);

        try {
            return userMapper.toUserDto(userRepository.save(userMapper.toUser(userRequest)));
        } catch (Exception e)  {
            throw new ConflictException(String.format("Ошибка имя пользователя уже сущетсвует post name " +
                    "= {}", userRequest.getName()));
        }
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        log.info("Удаление пользователя userId = {} AdminUserServiceImpl.delete", userId);

        userRepository
                .delete(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден " +
                        "delete id = %s", userId))));
    }

}
