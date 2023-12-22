package com.digsol.main.event;

import com.digsol.main.user.UserShortDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.digsol.main.category.CategoryDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;

    @NotBlank
    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests = 0;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotBlank
    private UserShortDto initiator;

    @NotNull
    private Boolean paid;

    @NotBlank
    private String title;

    private Integer views;

    private Integer rate;

    @Override
    public String toString() {
        return "EventShortDto{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", eventDate=" + eventDate +
                ", initiator=" + initiator +
                ", paid=" + paid +
                ", title='" + title + '\'' +
                '}';
    }
}