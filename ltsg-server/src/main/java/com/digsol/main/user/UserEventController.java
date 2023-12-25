package com.digsol.main.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.digsol.main.event.EventFullDto;
import com.digsol.main.event.EventMapper;
import com.digsol.main.event.EventShortDto;
import com.digsol.main.event.NewEventDto;
import com.digsol.main.request.EventRequestStatusUpdateRequest;
import com.digsol.main.request.EventRequestStatusUpdateResult;
import com.digsol.main.request.RequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/events")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEventController {

    private final UserEventService userEventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getUserEvents(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.debug("Events requested for user {}", userId);

        List<EventShortDto> events = userEventService.getUserEvents(userId, from, size);

        return new ResponseEntity<>(events, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable Long userId,
                                                 @Valid @RequestBody NewEventDto newEventDto) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(
                userEventService.addEvent(userId, newEventDto),
                0, 0, 0);

        log.debug("User {} requested add event", userId);

        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {

        EventFullDto eventFullDto = userEventService.getUserEvent(userId, eventId);

        log.debug("Event with id {} requested for user {}", eventId, userId);

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long userId,
                                                    @PathVariable Long eventId,
                                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest
    ) {
        EventFullDto eventFullDto = userEventService.updateUserEvent(userId, eventId, updateEventUserRequest);

        log.debug("User {} requested update event with id {}", userId, eventId);

        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<Collection<RequestDto>> getEventRequest(@PathVariable Long userId,
                                                                  @PathVariable Long eventId) {

        Collection<RequestDto> requestDtos = userEventService.getUserEventRequests(userId, eventId);

        log.debug("Event requests requested for user {}", userId);

        return new ResponseEntity<>(requestDtos, HttpStatus.OK);

    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventRequest(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        EventRequestStatusUpdateResult rs = userEventService.updateUserEventRequest(userId, eventId, eventRequestStatusUpdateRequest);
        log.debug("Event {} requests status update result requested for user {}", eventId, userId);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }
}