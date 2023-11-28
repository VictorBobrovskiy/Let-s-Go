package com.digsol.main.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@Slf4j
public class UserCommentController {

    private final CommentServiceImpl commentService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@Valid @RequestBody CommentDto commentDto,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long eventId) {

        return new ResponseEntity<>(commentService.addComment(commentDto, userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentDto> updateComment(@Valid @RequestBody CommentDto commentDto,
                                                    @PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @PathVariable Long commentId) {

        return new ResponseEntity<>(
                commentService.updateComment(userId, eventId, commentId, commentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public String deleteComment(@PathVariable Long userId,
                                @PathVariable Long eventId,
                                @PathVariable Long commentId) {
        commentService.deleteComment(userId, eventId, commentId);
        return "Комментарий удален";
    }
}
