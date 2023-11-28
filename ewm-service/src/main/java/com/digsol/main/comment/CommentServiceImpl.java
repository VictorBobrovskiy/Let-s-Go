package com.digsol.main.comment;

import com.digsol.main.event.EventNotFoundException;
import com.digsol.main.event.EventRepository;
import com.digsol.main.user.User;
import com.digsol.main.user.UserNotFoundException;
import com.digsol.main.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.main.event.Event;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long eventId, int from, int size) {

        List<Comment> comments = commentRepository.findAllByEvent_Id(
                eventId, PageRequest.of(from / size, size)).getContent();

        return CommentMapper.toCommentDtos(comments);
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long eventId) {

        Comment comment = CommentMapper.toComment(commentDto);

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found"));

        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EventNotFoundException("Event " + eventId + " not found"));

        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));

        commentRepository.save(comment);

        return CommentMapper.toCommentDto(comment);
    }

    @Override
    public CommentDto updateComment(Long userId, Long eventId, Long commentId,
                                    CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndEventIdAndAuthorId(commentId, eventId, userId)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(Long eventId, Long commentId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndEventId(commentId, eventId).orElseThrow(
                () -> new CommentNotFoundException("Comment not found"));
        comment.setText(commentDto.getText());
        comment.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS));
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long eventId, Long commentId) {
        if (!commentRepository.existsByIdAndEventId(commentId, eventId)) {
            throw new CommentNotFoundException("Comment not found");
        }
        commentRepository.deleteByIdAndEventId(commentId, eventId);
    }

    @Override
    public void deleteComment(Long userId, Long eventId, Long commentId) {
        if (!commentRepository.existsByIdAndEventIdAndAuthorId(commentId, eventId, userId)) {
            throw new CommentNotFoundException("Comment not found");
        }
        commentRepository.deleteByIdAndEventIdAndAuthorId(commentId, eventId, userId);
    }

}
