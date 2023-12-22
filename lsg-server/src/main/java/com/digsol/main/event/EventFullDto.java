package com.digsol.main.event;

import com.digsol.main.user.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.digsol.main.category.CategoryDto;
import com.digsol.main.location.LocationDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Boolean paid;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private UserDto initiator;

    @NotBlank
    private String description;

    private Integer participantLimit;

    private EventState state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @NotNull
    private LocationDto location;

    private Boolean requestModeration = true;

    private Integer confirmedRequests = 0;

    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private Integer views;

    private Integer rate;

    @Override
    public String toString() {
        return "EventFullDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", annotation='" + annotation + '\'' +
                ", paid=" + paid +
                ", eventDate=" + eventDate +
                '}';
    }
}