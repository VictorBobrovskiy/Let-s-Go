package com.digsol.main.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.ValidationException;
import java.util.Collection;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminEventsController {

    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<Collection<EventFullDto>> getEvents(
            @RequestParam(required = false) Collection<Long> users,
            @RequestParam(required = false) Collection<EventState> states,
            @RequestParam(required = false) Collection<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) throws ValidationException {
        Collection<EventFullDto> events = adminEventService.getAdminEvents(
                users, states, categories, rangeStart, rangeEnd, from, size);
        log.debug("----- Events requested by admin");
        return ResponseEntity.ok(events);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEvent(@PathVariable Long eventId,
                                                    @Validated @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.debug("----- Event update requested by admin");
        return ResponseEntity.ok(adminEventService.updateAdminEvent(eventId, updateEventAdminRequest));
    }
}