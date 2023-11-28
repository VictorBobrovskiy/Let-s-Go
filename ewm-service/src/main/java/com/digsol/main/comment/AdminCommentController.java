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
@RequestMapping("/admin/events/{eventId}/comments")
@Slf4j
public class AdminCommentController {

    private final CommentServiceImpl commentService;

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentDto> updateComment(

            @Valid @RequestBody CommentDto commentDto,
            @PathVariable Long eventId,
            @PathVariable Long commentId) {

        return new ResponseEntity<>(
                commentService.updateComment(eventId, commentId, commentDto), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long eventId, @PathVariable Long commentId) {

        commentService.deleteComment(eventId, commentId);
    }
}
