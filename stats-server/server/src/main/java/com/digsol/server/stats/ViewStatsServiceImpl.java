package com.digsol.server.stats;

import com.digsol.server.error.ValidationException;
import com.digsol.server.hit.EndpointHitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ViewStatsServiceImpl implements ViewStatsService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EndpointHitRepository endpointHitRepository;


    @Override
    public Collection<ViewStatsDto> getStats(String start,
                                             String end,
                                             Collection<String> uris,
                                             boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse(end, FORMATTER);

        checkDateValidity(startDate, endDate);

        if (uris == null) uris = Collections.emptyList();

        log.debug("----- Statistics for the endpoint provided");

        if (unique) return endpointHitRepository.getViewStatsUnique(startDate, endDate, uris);

        return endpointHitRepository.getViewStats(startDate, endDate, uris);
    }

    private void checkDateValidity(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(end)) {
            throw new ValidationException("Start must be before end");
        }
    }
}