package com.digsol.main.event;

import javax.xml.bind.ValidationException;
import java.util.Collection;

public interface AdminEventService {
    Collection<EventFullDto> getAdminEvents(Collection<Long> users,
                                            Collection<EventState> states,
                                            Collection<Long> categories,
                                            String rangeStart,
                                            String rangeEnd,
                                            Integer from,
                                            Integer size) throws ValidationException;

    EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
