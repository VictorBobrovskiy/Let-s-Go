package com.digsol.main.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    @NotBlank
    @Length(min = 1, max = 50)
    private String title;

    private Collection<Long> events = new ArrayList<>();

    private Boolean pinned = false;
}