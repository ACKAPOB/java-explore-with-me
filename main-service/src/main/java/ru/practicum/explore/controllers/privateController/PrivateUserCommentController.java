package ru.practicum.explore.controllers.privateController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;
import ru.practicum.explore.feature.comment.service.PrivateUserCommentService;


@RestController
@RequestMapping("/users/{userId}")
@Slf4j
public class PrivateUserCommentController {

    PrivateUserCommentService privateUserCommentService;

    @PostMapping("/events/{eventId}/comment")
    public CommentDto postComment(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @RequestBody CommentDto commentDto) {
        log.info("Добавление комментария пользователем userId = {}, eventId = {} " +
                "PrivateUserCommentController.postComment", userId, eventId);
        return privateUserCommentService.post(userId, eventId, commentDto);
    }

    @DeleteMapping("/comment/{comId}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long comId) {
        log.info("Удаление коментария PrivateUserCommentController.deleteComment userId ={}, comId ={}", userId, comId);
        privateUserCommentService.delete(userId, comId);
    }

    @PatchMapping("/events/{eventId}/comment")
    public CommentDto patchComment(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody UpdateComment updateComment) {
        log.info("Обновление коментария PrivateUserCommentController.patchComment userId = {}, eventId = {}", userId, eventId);
        return privateUserCommentService.patch(userId, eventId, updateComment);
    }

    @GetMapping("/comment/{comId}")
    public void getComment(@PathVariable Long comId, @PathVariable Long userId) {
        log.info("Получение коментария PrivateUserCommentController.getComment comId = {}", comId);
        privateUserCommentService.get(comId, userId);
    }

}

