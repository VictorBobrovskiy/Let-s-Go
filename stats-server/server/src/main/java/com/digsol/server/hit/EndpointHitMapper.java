package com.digsol.server.hit;

import lombok.experimental.UtilityClass;
import com.digsol.dto.EndpointHitDto;
import com.digsol.server.app.App;

@UtilityClass
public class EndpointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto, App app) {
        return new EndpointHit(
                app,
                endpointHitDto.getUri(),
                endpointHitDto.getIp(),
                endpointHitDto.getTimestamp()
        );
    }
}