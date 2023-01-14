package ru.practicum.explore.controllers.privateController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;
import ru.practicum.explore.feature.comment.service.PrivateUserCommentService;


@RestController
@RequestMapping("users/{userId}/")
@Slf4j
public class PrivateUserCommentController {

    PrivateUserCommentService privateUserCommentService;

    // users/{userId}/events/{eventId}/comment
    // users/{userId}/comment/{comId}
    // users/{userId}/events/{eventId}/comment

    @PostMapping("{events/{eventId}/comment")
    public CommentDto postComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody CommentDto commentDto) {
        log.info("Добавление комментария пользователем userId = {}, eventId = {} " +
                "PrivateUserCommentController.postComment", userId, eventId);
        return privateUserCommentService.postComment(userId, eventId, commentDto);
    }

    @DeleteMapping("{userId}/comment/{comId}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long comId) {
        log.info("delete comment by userId={} and comId{}", userId, comId);
        privateUserCommentService.deleteComment(userId, comId);
    }

    @PatchMapping("{userId}/events/{eventId}/comment")
    public CommentDto patchComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @RequestBody UpdateComment updateComment) {
        log.info("update comment by userId={} and eventId{}", userId, eventId);
        return privateUserCommentService.patchComment(userId, eventId, updateComment);
    }

}

