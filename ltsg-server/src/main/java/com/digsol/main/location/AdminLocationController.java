package com.digsol.main.location;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/admin/locations")
@Validated
@Slf4j
public class AdminLocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationDto> addLocation(@Valid @RequestBody LocationDto locationDto) {
        log.debug("----- Location add requested");
        return new ResponseEntity(locationService.addLocation(locationDto), HttpStatus.CREATED);
    }

    @PatchMapping("/{locationId}")
    public ResponseEntity<LocationDto> updateLocation(@PathVariable(name = "locationId") Long locationId,
                                                      @RequestBody LocationDto locationDto) {
        log.debug("----- Location update requested");
        return new ResponseEntity(locationService.updateLocation(locationId, locationDto), HttpStatus.OK);
    }
}