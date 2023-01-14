package ru.practicum.explore.feature.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.feature.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventOrderByCreated(Event event);
}
