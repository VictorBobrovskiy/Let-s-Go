package com.digsol.main.compilation;

import com.digsol.main.event.EventRepository;
import com.digsol.main.event.EventServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.main.event.Event;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    private final EventRepository eventRepository;

    private final EventServiceImpl eventService;

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {

        Collection<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());

        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto, events));

        log.debug("----- Compilation {} created", compilation.getId());

        return CompilationMapper.toCompilationDto(compilation, eventService.mapToEventShorts(events));
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (compilationRepository.findById(compId).isEmpty()) {
            throw new CompilationNotFoundException("Compilation " + compId + "not found");
        }

        compilationRepository.deleteById(compId);
        log.debug("----- Compilation {} deleted", compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation " + compId + "not found"));

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        }

        Compilation updatedCompilation = compilationRepository.save(compilation);

        log.debug("----- Compilation {} updated", compId);

        return CompilationMapper.toCompilationDto(updatedCompilation,
                eventService.mapToEventShorts(updatedCompilation.getEvents()));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        Collection<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable).toList();

        Collection<CompilationDto> compilationDtos = compilations.stream()
                .map(compilation -> CompilationMapper.toCompilationDto(compilation, eventService.mapToEventShorts(compilation.getEvents())))
                .collect(Collectors.toList());

        log.debug("----- {} Compilations found", compilationDtos.size());

        return compilationDtos;
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Compilation " + compId + "not found"));
        log.info("----- Compilation {} found", compId);

        return CompilationMapper.toCompilationDto(compilation,
                eventService.mapToEventShorts(compilation.getEvents()));
    }
}