package com.digsol.server.hit;

import com.digsol.dto.EndpointHitDto;

public interface EndpointHitService {
    void saveHit(EndpointHitDto endpointHitDto);
}
