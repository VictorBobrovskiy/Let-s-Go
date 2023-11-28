package com.digsol.server.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.digsol.dto.EndpointHitDto;

@Slf4j
@RestController
@RequestMapping("/hit")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EndpointHitController {

    private final EndpointHitService endpointHitService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void saveEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.debug("----- Save EndpointHit stat requested");
        endpointHitService.saveHit(endpointHitDto);
    }
}