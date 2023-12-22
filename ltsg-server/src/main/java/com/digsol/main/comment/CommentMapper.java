package com.digsol.main.comment;

import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(CommentDto dto) {
        return new Comment(
                dto.getId(),
                dto.getText(),
                dto.getCreated()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> toCommentDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

}
