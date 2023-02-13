package ru.practicum.explore.feature.comment.service;


import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;

public interface PrivateUserCommentService {

    CommentDto post(Long userId, Long eventId, CommentDto commentDto);

    void delete(Long userId, Long comId);

    CommentDto patch(Long userId, Long eventId, UpdateComment updateComment);

    CommentDto get(Long comId, Long userId);
}
