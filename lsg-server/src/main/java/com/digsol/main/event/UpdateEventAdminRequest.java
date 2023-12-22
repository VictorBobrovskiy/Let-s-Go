package com.digsol.main.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import com.digsol.main.location.LocationDto;

import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest {

    private AdminUpdateEventState stateAction;

    @Length(min = 20, max = 2000)
    private String annotation;

    private Long category;

    @Length(min = 20, max = 7000)
    private String description;

    @FutureOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    @Length(min = 3, max = 120)
    private String title;

    public UpdateEventAdminRequest(AdminUpdateEventState stateAction) {
        this.stateAction = stateAction;
    }

    @Override
    public String toString() {
        return "UpdateEventAdminRequest{" +
                "stateAction=" + stateAction +
                '}';
    }
}