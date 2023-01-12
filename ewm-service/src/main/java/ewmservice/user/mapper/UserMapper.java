package ewmservice.user.mapper;

import ewmservice.user.dto.UserDto;
import ewmservice.user.dto.UserShortDto;
import ewmservice.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User toUserFromShort(UserShortDto userShortDto) {
        return User.builder()
                .id(userShortDto.getId())
                .name(userShortDto.getName())
                .build();
    }
}
