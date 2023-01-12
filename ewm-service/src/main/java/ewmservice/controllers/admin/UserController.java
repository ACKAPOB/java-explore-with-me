package ewmservice.controllers.admin;

import ewmservice.user.dto.UserDto;
import ewmservice.user.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static ewmservice.utilities.Validator.validateUserName;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public UserDto saveUser(@RequestBody UserDto userDto) {
        validateUserName(userDto);
        log.info("Creating user={}", userDto);
        return userService.saveUser(userDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(name = "ids") List<Long> ids) {
        log.info("Get users={}", ids);
        return userService.getUsers(ids);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Delete user={}", userId);
        userService.deleteUser(userId);
    }
}
