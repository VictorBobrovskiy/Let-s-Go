package com.digsol.main.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/admin/compilations")
@Validated
public class AdminCompilationController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.debug("----- Create compilation requested");
        return new ResponseEntity<>(compilationService.createCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.debug("----- Delete compilation requested");
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId,
                                                            @Valid @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.debug("----- Update compilation requested");
        return new ResponseEntity<>(compilationService.updateCompilation(compId, updateCompilationRequest), HttpStatus.OK);
    }
}