package com.digsol.server.stats;

import com.digsol.server.error.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.digsol.dto.ViewStatsDto;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ViewStatsController {

    private final ViewStatsService viewStatsService;

    @GetMapping
    public ResponseEntity<Collection<ViewStatsDto>> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) Collection<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) throws ValidationException {
        log.debug("----- Stats requested");
        return new ResponseEntity<>(viewStatsService.getStats(start, end, uris, unique), HttpStatus.OK);
    }
}