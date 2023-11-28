package com.digsol.main.request;

import com.digsol.main.event.EventUpdateRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {

    private final Collection<Long> requestIds = new ArrayList<>();

    private EventUpdateRequestStatus status;
}