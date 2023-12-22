package com.digsol.main.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestController {

    private final RequestServiceImpl requestsService;

    @GetMapping
    public ResponseEntity<Collection<RequestDto>> getRequests(@PathVariable Long userId) {
        log.debug("----- Requests for user with id {} requested", userId);
        return new ResponseEntity<>(requestsService.getRequests(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<RequestDto> addRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.debug("----- Add request for user with id {} requested", userId);
        return new ResponseEntity<>(requestsService.addRequest(userId, eventId), HttpStatus.CREATED);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        log.debug("----- Cancel request with id {} for user with id {} requested", requestId, userId);
        return new ResponseEntity<>(requestsService.cancelRequest(userId, requestId), HttpStatus.OK);
    }
}