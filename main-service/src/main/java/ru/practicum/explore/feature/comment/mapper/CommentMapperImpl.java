package ru.practicum.explore.feature.comment.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;
import ru.practicum.explore.feature.comment.model.Comment;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }

    @Override
    public Comment toComment(CommentDto commentDto, User user, Event event) {
        return Comment.builder()
                .id(commentDto.getId())
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .build();
    }

    @Override
    public void updateCommentFromUpdateComment(UpdateComment updateComment, Comment comment) {
        if (updateComment.getText() != null) {
            comment.setText(updateComment.getText());
        }
    }
}
