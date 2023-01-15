package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.service.AdminUserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение информации о пользователях AdminUserController.getAllUsers");
        return adminUserService.getAllUsers(ids, from, size);
    }

    @PostMapping
    public UserDto postUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Добавление нового пользователя newUserRequest = {} AdminUserController.postUser", newUserRequest);
        return adminUserService.postUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя userId = {} AdminUserController.deleteUser", userId);
        adminUserService.deleteUser(userId);
    }

}
