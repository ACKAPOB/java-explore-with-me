package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.ewm.dto.UserDto;
import ru.practicum.ewm.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Validated @RequestBody UserDto userDto) {
        log.info("Создание пользователя UserController.createUser userDto userDto = {}",
                userDto);
        return userService.createUser(userDto);
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name = "ids") List<Long> ids) {
        log.info("Поиск пользователей UserController.getUsers, ids = {}", ids);
        return userService.getUsers(ids);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя  UserController.deleteUser id = {}", userId);
        userService.deleteUser(userId);
    }
}
