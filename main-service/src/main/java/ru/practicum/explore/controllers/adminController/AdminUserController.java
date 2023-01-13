package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.service.EventService;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
public class AdminUserController {

    private final EventService eventService;

    @Autowired
    public AdminUserController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение информации о пользователях AdminUserController.getAllUsers");
        return eventService.getAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    public UserDto postUser(@RequestBody NewUserRequest newUserRequest) {
        log.info("Добавление нового пользователя newUserRequest = {} AdminUserController.postUser", newUserRequest);
        return eventService.postUser(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя userId = {} AdminUserController.deleteUser", userId);
        eventService.deleteUser(userId);
    }

}
