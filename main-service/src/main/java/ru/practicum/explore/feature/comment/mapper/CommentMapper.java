package ru.practicum.explore.feature.comment.mapper;


import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;
import ru.practicum.explore.feature.comment.model.Comment;
import ru.practicum.explore.user.model.User;

public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto, User user, Event event);

    void updateCommentFromUpdateComment(UpdateComment updateComment, Comment comment);
}
