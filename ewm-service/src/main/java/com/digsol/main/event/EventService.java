package com.digsol.main.event;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.Collection;
import java.util.List;

public interface EventService {

    List<EventShortDto> mapToEventShorts(Collection<Event> events);

    List<EventShortDto> getEventsInLocation(Long locationId, String sort, Integer from, Integer size);

    List<EventShortDto> getEvents(String text,
                                  Collection<Long> categories,
                                  Boolean paid,
                                  String rangeStart,
                                  String rangeEnd,
                                  Boolean onlyAvailable,
                                  String sort,
                                  Integer from,
                                  Integer size,
                                  Double lat,
                                  Double lon,
                                  Double distance,
                                  HttpServletRequest request) throws ValidationException;

    EventFullDto getEvent(Long id, HttpServletRequest request);


}
