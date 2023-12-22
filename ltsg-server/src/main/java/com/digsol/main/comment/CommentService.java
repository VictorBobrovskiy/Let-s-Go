package com.digsol.main.comment;

import java.util.List;

public interface CommentService {

    List<CommentDto> getComments(Long eventId, int from, int size);

    CommentDto addComment(CommentDto commentDto, Long userId, Long eventId);

    CommentDto updateComment(Long userId, Long eventId, Long commentId, CommentDto commentDto);

    CommentDto updateComment(Long eventId, Long commentId, CommentDto commentDto);

    void deleteComment(Long userId, Long eventId, Long commentId);

    void deleteComment(Long eventId, Long commentId);
}
