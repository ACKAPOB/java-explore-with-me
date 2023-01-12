package ewmservice.user.service;

import ewmservice.user.dto.UserDto;
import ewmservice.user.model.User;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);

    List<UserDto> getUsers(List<Long> ids);

    void deleteUser(Long userId);

    User getUser(Long userId);
}
