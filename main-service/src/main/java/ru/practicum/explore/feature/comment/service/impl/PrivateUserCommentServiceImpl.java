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
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено postComment " +
                        "id = %s", eventId)));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден postComment " +
                        "id = %s", userId)));
        if (commentDto.getText().isEmpty())
            throw new ErrorRequestException("Ошибка - текст комментария пуст postComment");
        if (event.getState() != Status.PUBLISHED)
            throw new ErrorRequestException("Ошибка - событие не опубликовано postComment");
        return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(commentDto, user, event)));
    }

    @Override
    public void deleteComment(Long userId, Long comId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден delete Comment " +
                        "id = %s", userId)));
        if (!Objects.equals(commentRepository
          .findById(comId).orElseThrow(() -> new ObjectNotFoundException(String.format("Комментарий не найден " +
                                "patchComment " + "id = %s", comId)))
          .getId(),
          userId))
            throw new ErrorRequestException("Ошибка - вы не автор комментария deleteComment");
        commentRepository.deleteById(comId);
    }

    @Override
    public CommentDto patchComment(Long userId, Long eventId, UpdateComment updateComment) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден patchComment " +
                        "id = %s", userId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено patchComment " +
                        "id = %s", eventId)));
        Comment comment = commentRepository
                .findById(updateComment.getId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Комментарий не найден patchComment " +
                                "id = %s", updateComment.getId())));
        if (event.getState() != Status.PUBLISHED)
            throw new ErrorRequestException("Ошибка - событие не опубликовано patchComment");
        if (updateComment.getText().isEmpty())
            throw new ErrorRequestException("Ошибка - текст комментария пуст patchComment");
        if (!Objects.equals(comment.getId(), userId))
            throw new ErrorRequestException("Ошибка - вы не автор комментария patchComment");
        commentMapper.updateCommentFromUpdateComment(updateComment, comment);
        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto getComment(Long comId, Long userId) {
        return commentRepository.findById(comId).map(commentMapper::toCommentDto)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Комментарий не найден getComment " +
                "id = %s", comId)));
    }
}
