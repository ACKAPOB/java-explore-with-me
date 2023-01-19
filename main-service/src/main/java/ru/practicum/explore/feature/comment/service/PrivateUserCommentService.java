package ru.practicum.explore.feature.comment.service;


import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;

public interface PrivateUserCommentService {

    CommentDto postComment(Long userId, Long eventId, CommentDto commentDto);

    void deleteComment(Long userId, Long comId);

    CommentDto patchComment(Long userId, Long eventId, UpdateComment updateComment);

    CommentDto getComment(Long comId, Long userId);
}
