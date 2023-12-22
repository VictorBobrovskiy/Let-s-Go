package com.digsol.main.compilation;

import com.digsol.main.event.EventShortDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private Long id;

    @NotNull
    private Boolean pinned;

    @NotBlank
    private String title;

    private Collection<EventShortDto> events;
}