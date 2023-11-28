package com.digsol.server.hit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.dto.EndpointHitDto;
import com.digsol.server.app.App;
import com.digsol.server.app.AppRepository;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;

    private final AppRepository appRepository;

    @Override
    public void saveHit(EndpointHitDto endpointHitDto) {
        Optional<App> savedApp = appRepository.findByName(endpointHitDto.getApp());
        App app = savedApp.orElseGet(() -> appRepository.save(new App(endpointHitDto.getApp())));
        EndpointHit endpointHit = endpointHitRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto, app));
        log.debug("----- EndpointHit with id {} saved", endpointHit.getId());
    }
}