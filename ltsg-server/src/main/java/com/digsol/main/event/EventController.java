package com.digsol.main.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.xml.bind.ValidationException;
import java.util.Collection;

@RestController
@RequestMapping("/events")
@Validated
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Collection<EventShortDto>> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Collection<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(defaultValue = "false") Boolean available,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double distance,
            HttpServletRequest request) throws ValidationException {
        Collection<EventShortDto> eventList = eventService.getEvents(text, categories, paid, rangeStart, rangeEnd,
                available, sort, from, size, lat, lon, distance, request);
        log.debug("----- Events requested");
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getEvent(@PathVariable Long id, HttpServletRequest request) {

        log.debug("----- Event with id {} requested", id);

        return new ResponseEntity<>(eventService.getEvent(id, request), HttpStatus.OK);
    }
}