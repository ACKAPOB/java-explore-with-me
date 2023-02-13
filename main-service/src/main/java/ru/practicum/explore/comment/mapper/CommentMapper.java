package ru.practicum.explore.comment.mapper;


import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.comment.dto.UpdateComment;
import ru.practicum.explore.comment.model.Comment;
import ru.practicum.explore.user.model.User;

public interface CommentMapper {

    CommentDto toCommentDto(Comment comment);

    Comment toComment(CommentDto commentDto, User user, Event event);

    void updateCommentFromComment(UpdateComment updateComment, Comment comment);
}
