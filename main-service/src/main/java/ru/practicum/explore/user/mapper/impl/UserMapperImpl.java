package ru.practicum.explore.user.mapper.impl;

import org.springframework.stereotype.Component;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.dto.UserDto.UserDtoBuilder;
import ru.practicum.explore.user.dto.UserShortDto;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.user.model.User;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public UserDto toUserDto(User user) {
        UserDtoBuilder userDto = UserDto.builder();
        userDto.id(user.getId());
        userDto.name(user.getName());
        userDto.email(user.getEmail());
        return userDto.build();
    }

    @Override
    public User toUser(NewUserRequest newUserRequest) {

        return User.builder()
                .email(newUserRequest.getEmail())
                .name(newUserRequest.getName())
                .build();
    }

    @Override
    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
