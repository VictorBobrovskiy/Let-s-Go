package com.digsol.main.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import com.digsol.main.location.LocationDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {

    @NotBlank
    @Length(min = 3, max = 120)
    private String title;

    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;

    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private LocationDto location;

    private boolean paid = false;

    private Integer participantLimit = 0;

    private boolean requestModeration = true;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "title='" + title + '\'' +
                ", annotation='" + annotation + '\'' +
                ", categoryId=" + category +
                ", eventDate=" + eventDate +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                '}';
    }
}