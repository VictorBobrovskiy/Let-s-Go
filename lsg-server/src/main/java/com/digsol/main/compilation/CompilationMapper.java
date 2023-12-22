package com.digsol.main.compilation;

import com.digsol.main.event.EventShortDto;
import lombok.experimental.UtilityClass;
import com.digsol.main.event.Event;

import java.util.Collection;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto, Collection<Event> events) {
        return new Compilation(newCompilationDto.getPinned(), newCompilationDto.getTitle(), events);

    }

    public static CompilationDto toCompilationDto(Compilation compilation, Collection<EventShortDto> events) {
        return new CompilationDto(compilation.getId(), compilation.getPinned(), compilation.getTitle(), events);
    }
}