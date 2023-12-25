package com.digsol.main.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CommentController {

    private final CommentServiceImpl commentService;


    @GetMapping
    public ResponseEntity<Collection<CommentDto>> getCommentsToEvent(
            @PathVariable Long eventId,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {

        return new ResponseEntity<>(commentService.getComments(eventId, from, size), HttpStatus.OK);
    }
}
