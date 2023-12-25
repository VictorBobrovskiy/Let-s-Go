package com.digsol.main.stat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.client.StatClient;
import com.digsol.dto.ViewStatsDto;
import com.digsol.main.event.Event;
import com.digsol.main.request.RequestCount;
import com.digsol.main.request.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatServiceImpl implements StatService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RequestRepository requestRepository;

    private final StatClient statClient;

    private final ObjectMapper objectMapper;


    @Override
    public Map<Long, Integer> getConfirmedRequests(Collection<Event> events) {

        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());

        Collection<RequestCount> requestCountCollection = requestRepository.getConfirmedRequests(eventIds);

        Map<Long, Integer> confirmedRequests = requestCountCollection.stream()
                .collect(Collectors.toMap(RequestCount::getEventId, requestCount ->
                        requestCount.getConfirmedRequestsCount().intValue()));

        log.debug("----- {} confirmed requests sent", confirmedRequests.size());

        return confirmedRequests;
    }

    @Override
    public Map<Long, Integer> getViews(Collection<Event> events) {

        Map<Long, Integer> views = new HashMap<>();

        LocalDateTime start = events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        List<String> uris = events.stream()
                .map(event -> "/events/" + event.getId())
                .collect(Collectors.toList());

        ResponseEntity<Object> response = statClient.getViewStats(
                start.format(FORMATTER),
                LocalDateTime.now().format(FORMATTER),
                uris,
                true
        );

        try {
            Collection<ViewStatsDto> viewStats = Arrays.asList(objectMapper.readValue(
                    objectMapper.writeValueAsString(response.getBody()), ViewStatsDto[].class));

            viewStats.stream()
                    .filter(viewStatsDto -> viewStatsDto.getUri().split("/").length > 2)
                    .forEach(viewStatsDto -> views.put(
                            Long.parseLong(viewStatsDto.getUri().split("/")[2]),
                            viewStatsDto.getHits()));

            log.debug("----- View stats for events provided");
            return views;
        } catch (JsonProcessingException exception) {
            throw new StatNotFoundException("Could not load stats");
        }
    }

    @Transactional
    @Override
    public void addHit(HttpServletRequest request) {
        log.debug("----- View stats for event added");
        statClient.addHit(request);
    }
}