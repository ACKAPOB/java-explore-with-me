package ru.practicum.explore.feature.comment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.feature.comment.dto.CommentDto;
import ru.practicum.explore.feature.comment.dto.UpdateComment;
import ru.practicum.explore.feature.comment.mapper.CommentMapper;
import ru.practicum.explore.feature.comment.model.Comment;
import ru.practicum.explore.feature.comment.repository.CommentRepository;
import ru.practicum.explore.feature.comment.service.PrivateUserCommentService;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;

import java.util.Objects;


@Service
@Slf4j
@AllArgsConstructor
class PrivateUserCommentServiceImpl implements PrivateUserCommentService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto postComment(Long userId, Long eventId, CommentDto commentDto) {
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        if (commentDto.getText().isEmpty())
            throw new ErrorRequestException("Ошибка - текст комментария пуст");
        if (event.getState() != Status.PUBLISHED)
            throw new ErrorRequestException("Ошибка - событие не опубликовано");
        Comment comment = commentMapper.toComment(commentDto, user, event);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Long comId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        Comment comment = commentRepository
                .findById(comId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Comment not found id = %s", comId)));
        if (!Objects.equals(comment.getId(), userId))
            throw new ErrorRequestException("Ошибка - вы не автор комментария");
        commentRepository.deleteById(comId);
    }

    @Override
    public CommentDto patchComment(Long userId, Long eventId, UpdateComment updateComment) {

        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));

        Comment comment = commentRepository
                .findById(updateComment.getId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Comment not found id = %s",
                        updateComment.getId())));

        if (event.getState() != Status.PUBLISHED)
            throw new ErrorRequestException("Ошибка - событие не опубликовано");
        if (updateComment.getText().isEmpty())
            throw new ErrorRequestException("Ошибка - текст комментария пуст");
        if (!Objects.equals(comment.getId(), userId))
            throw new ErrorRequestException("Ошибка - вы не автор комментария");
        commentMapper.updateCommentFromUpdateComment(updateComment, comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }
}
