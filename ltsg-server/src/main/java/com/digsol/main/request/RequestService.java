package com.digsol.main.request;

import java.util.Collection;

public interface RequestService {
    Collection<RequestDto> getRequests(Long userId);

    RequestDto addRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
