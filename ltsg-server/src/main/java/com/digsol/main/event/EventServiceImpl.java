package com.digsol.main.event;

import com.digsol.main.error.ValidationException;
import com.digsol.main.stat.StatService;
import com.digsol.main.user.UserMapper;
import com.digsol.main.user.UserShortDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.main.category.CategoryDto;
import com.digsol.main.category.CategoryMapper;
import com.digsol.main.rating.LikeService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventServiceImpl implements EventService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;

    private final StatService statService;

    private final LikeService likeService;

    @Override
    public List<EventShortDto> getEventsInLocation(Long locationId, String sort, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sort).ascending());

        List<Event> events = eventRepository.findAllEventsByLocationId(locationId, pageable).toList();

        return mapToEventShorts(events);
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {

        Event event = getEvent(eventId);

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new EventNotFoundException("Event " + eventId + " not found");
        }

        statService.addHit(request);

        Map<Long, Integer> views = statService.getViews(List.of(event));

        Map<Long, Integer> confirmedRequests = statService.getConfirmedRequests(List.of(event));

        Integer rating = likeService.getRating(eventId);

        rating = null == rating ? 0 : rating;

        log.debug("----- Found event {}", eventId);

        return EventMapper.toEventFullDto(
                event,
                views.get(event.getId()),
                confirmedRequests.get(event.getId()),
                rating);
    }

    @Override
    public List<EventShortDto> getEvents(String text,
                                         Collection<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Integer from,
                                         Integer size,
                                         Double userLat,
                                         Double userLon,
                                         Double distance,
                                         HttpServletRequest request) throws ValidationException {

        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, FORMATTER);

        validateDates(start, end);

        List<Event> events = new ArrayList<>();

        if (sort == null) sort = "ID";

        if (text != null && !"0".equals(text)) {
            text = text.toLowerCase();
        } else {
            text = "";
        }

        Pageable pageable = PageRequest.of(from / size, size);

        if (distance != null && userLat != null && userLon != null) {
            events = eventRepository.findAllByDistanceOrderByDistance(userLat, userLon, distance, pageable).toList();

        } else if (distance == null && "DISTANCE".equals(sort) && userLat != null && userLon != null) {
            events = eventRepository.findAllOrderByDistance(userLat, userLon, pageable).toList();
        } else if ("EVENT_DATE".equals(sort) || "VIEWS".equals(sort) || "RATE".equals(sort)) {
            events = eventRepository.findAllOrderByEventDate(
                    text, categories, paid, onlyAvailable, start, end, pageable).toList();
        } else {
            events = eventRepository.findAllOrderById(text, categories, paid, onlyAvailable, start, end, pageable)
                    .toList();
        }

        statService.addHit(request);

        log.debug("----- Found {} events", events.size());

        List<EventShortDto> eventsShorts = mapToEventShorts(events);

        if ("RATE".equals(sort)) {
            eventsShorts.sort(Comparator.comparingInt(EventShortDto::getRate));

        } else if ("VIEWS".equals(sort)) {
            eventsShorts.sort(Comparator.comparingInt(EventShortDto::getViews));
        }
        return eventsShorts;
    }

    @Override
    public List<EventShortDto> mapToEventShorts(Collection<Event> events) {

        Map<Long, Integer> views = statService.getViews(events);

        Map<Long, Integer> confirmedRequests = statService.getConfirmedRequests(events);

        List<EventShortDto> eventShorts = new ArrayList<>();

        for (Event event : events) {

            CategoryDto categoryDto = CategoryMapper.toCategoryDto(event.getCategory());

            UserShortDto userShortDto = UserMapper.toUserShortDto(event.getInitiator());

            Integer rating = likeService.getRating(event.getId());

            rating = null == rating ? 0 : rating;

            EventShortDto eventShortDto = EventMapper.toEventShortDto(
                    event,
                    categoryDto,
                    userShortDto,
                    views.get(event.getId()),
                    confirmedRequests.get(event.getId()),
                    rating
            );

            eventShorts.add(eventShortDto);
        }
        return eventShorts;
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found"));
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) throws ValidationException {
        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Not valid date range");
        }
    }

}