package com.digsol.main.location;

import com.digsol.main.event.EventService;
import com.digsol.main.event.EventShortDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/locations")
@Validated
@Slf4j
public class LocationController {

    private final LocationService locationService;

    private final EventService eventService;

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDto> getLocation(@PathVariable Long locationId) {
        log.debug("----- Location with id {} requested", locationId);
        return ResponseEntity.ok(LocationMapper.toLocationDto(locationService.getLocation(locationId)));
    }

    @GetMapping("/{locationId}/events")
    public ResponseEntity<Collection<EventShortDto>> getEventsInLocation(
            @PathVariable Long locationId,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.debug("----- Location with id {} requested", locationId);
        return ResponseEntity.ok(eventService.getEventsInLocation(locationId, sort, from, size));
    }


    @GetMapping
    public ResponseEntity<Collection<Location>> getLocations(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double distance
    ) {
        log.debug("----- Location list requested");
        return ResponseEntity.ok(locationService.getAllLocations(text, sort, from, size, lat, lon, distance));
    }


}



