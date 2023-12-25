package com.digsol.main.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<Collection<CompilationDto>> getCompilations(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size
    ) {
        log.debug("----- Compilations of events requested");

        return new ResponseEntity<>(compilationService.getCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long compId) {

        log.debug("----- Compilation of events with id {} requested", compId);

        return new ResponseEntity<>(compilationService.getCompilation(compId), HttpStatus.OK);
    }
}