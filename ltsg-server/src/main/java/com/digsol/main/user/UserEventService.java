package com.digsol.main.user;

import com.digsol.main.event.Event;
import com.digsol.main.event.EventFullDto;
import com.digsol.main.event.EventShortDto;
import com.digsol.main.event.NewEventDto;
import com.digsol.main.request.EventRequestStatusUpdateRequest;
import com.digsol.main.request.EventRequestStatusUpdateResult;
import com.digsol.main.request.RequestDto;

import java.util.Collection;
import java.util.List;

public interface UserEventService {

    Event addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEvent(Long userId, Long eventId);

    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventRequestStatusUpdateResult updateUserEventRequest(Long userId,
                                                          Long eventId,
                                                          EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    Collection<RequestDto> getUserEventRequests(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);
}
