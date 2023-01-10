package ru.practicum.ewm.user.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exception.ConflictException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;
import ru.practicum.ewm.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.user.mapper.UserMapper.toUser;
import static ru.practicum.ewm.user.mapper.UserMapper.toUserDto;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        log.info("Запрос на добавление User, UserServiceImpl.createUser" + userDto);
        try {
            return toUserDto(userRepository.save(toUser(userDto)));
        } catch (Exception e) {
            throw new ConflictException(String.format("%s - имя пользователя уже сущетсвует.", userDto.getName()));
        }
    }

    @Override
    @Transactional
    public List<UserDto> getUsers(List<Long> ids) {
        log.info("Запрос на поиск User с id = {}, UserServiceImpl.getUsers", ids);
        return userRepository
                .findAllById(ids)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Запрос на удаление User с id = {}, UserServiceImpl.deleteUser", userId);
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Ошибка удаления пользователь %d " +
                        "не найден", userId))));
    }

    @Override
    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь %d не найден", userId)));
    }
}
