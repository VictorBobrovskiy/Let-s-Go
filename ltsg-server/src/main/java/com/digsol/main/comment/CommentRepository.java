package com.digsol.main.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEvent_Id(Long eventId, Pageable pageable);

    void deleteByIdAndEventId(Long commentId, Long eventId);

    Boolean existsByIdAndEventIdAndAuthorId(Long commentId, Long eventId, Long authorId);

    Boolean existsByIdAndEventId(Long commentId, Long eventId);

    int deleteByIdAndEventIdAndAuthorId(Long commentId, Long eventId, Long authorId);

    Optional<Comment> findByIdAndEventId(Long commentId, Long eventId);

    Optional<Comment> findByIdAndEventIdAndAuthorId(Long commentId, Long eventId, Long userId);
}
