package com.digsol.main.rating;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/events/{eventId}/like")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LikeController {

    private final LikeService likeService;

    @PutMapping
    @ResponseStatus()
    public ResponseEntity<Like> addLike(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @RequestParam(name = "type") String type) {

        LikeType likeType = LikeType.from(type)
                .orElseThrow(() -> new IllegalArgumentException("Unknown like type: " + type));

        Like like = likeService.addLike(userId, eventId, likeType);

        return new ResponseEntity<>(like, HttpStatus.CREATED);
    }

    @DeleteMapping
    public String removeLike(@PathVariable Long userId,
                             @PathVariable Long eventId,
                             @RequestParam(name = "type") String type) {

        LikeType likeType = LikeType.from(type)
                .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));

        likeService.removeLike(userId, eventId, likeType);

        return "Like deleted";
    }

}