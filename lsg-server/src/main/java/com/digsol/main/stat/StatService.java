package com.digsol.main.stat;

import com.digsol.main.event.Event;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

public interface StatService {
    Map<Long, Integer> getConfirmedRequests(Collection<Event> events);

    Map<Long, Integer> getViews(Collection<Event> events);

    void addHit(HttpServletRequest request);
}
