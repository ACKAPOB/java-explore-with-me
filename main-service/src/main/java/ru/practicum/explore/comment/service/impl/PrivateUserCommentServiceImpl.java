package ru.practicum.explore.comment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.practicum.explore.comment.dto.CommentDto;
import ru.practicum.explore.comment.mapper.CommentMapper;
import ru.practicum.explore.comment.repository.CommentRepository;
import ru.practicum.explore.comment.service.PrivateUserCommentService;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.comment.dto.UpdateComment;
import ru.practicum.explore.comment.model.Comment;
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
    public CommentDto post(Long userId, Long eventId, CommentDto commentDto) {
        log.info("Создание комментария PrivateUserCommentServiceImpl.post commentDto = {}", commentDto);

        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено post " +
                        "id = %s", eventId)));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден post " +
                        "id = %s", userId)));
        if (commentDto.getText().isEmpty())
            throw new ErrorRequestException("Ошибка - текст комментария пуст post");
        if (event.getState() != Status.PUBLISHED)
            throw new ErrorRequestException("Ошибка - событие не опубликовано post");

        return commentMapper.toCommentDto(commentRepository.save(commentMapper.toComment(commentDto, user, event)));
    }

    @Override
    public void delete(Long userId, Long comId) {
        log.info("Удаление комментария PrivateUserCommentServiceImpl.post userId = {}, comId = {}", userId, comId);

        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден delete Comment " +
                        "id = %s", userId)));
        if (!Objects.equals(commentRepository
          .findById(comId).orElseThrow(() -> new ObjectNotFoundException(String.format("Комментарий не найден " +
                                "delete " + "id = %s", comId)))
          .getId(),
          userId))
            throw new ErrorRequestException("Ошибка - вы не автор комментария delete");
        commentRepository.deleteById(comId);
    }

    @Override
    public CommentDto patch(Long userId, Long eventId, UpdateComment updateComment) {
        log.info("Обновление комментария PrivateUserCommentServiceImpl.post userId = {}, eventId = {}", userId, eventId);

        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден patch " +
                        "id = %s", userId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено patch " +
                        "id = %s", eventId)));
        Comment comment = commentRepository
                .findById(updateComment.getId())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Комментарий не найден patch " +
                                "id = %s", updateComment.getId())));
        if (event.getState() != Status.PUBLISHED)
            throw new ErrorRequestException("Ошибка - событие не опубликовано patch");
        if (updateComment.getText().isEmpty())
            throw new ErrorRequestException("Ошибка - текст комментария пуст patch");
        if (!Objects.equals(comment.getId(), userId))
            throw new ErrorRequestException("Ошибка - вы не автор комментария patch");
        commentMapper.updateCommentFromComment(updateComment, comment);

        return commentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto get(Long comId, Long userId) {
        log.info("Получение комментария PrivateUserCommentServiceImpl.post userId = {}, comId = {}", userId, comId);

        return commentRepository.findById(comId).map(commentMapper::toCommentDto)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Комментарий не найден get " +
                "id = %s", comId)));
    }
}
