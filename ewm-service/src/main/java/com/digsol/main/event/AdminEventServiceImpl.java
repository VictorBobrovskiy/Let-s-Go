package com.digsol.main.event;

import com.digsol.main.error.AccessDeniedException;
import com.digsol.main.error.ValidationException;
import com.digsol.main.stat.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.main.category.Category;
import com.digsol.main.category.CategoryNotFoundException;
import com.digsol.main.category.CategoryRepository;
import com.digsol.main.location.Location;
import com.digsol.main.location.LocationDto;
import com.digsol.main.location.LocationMapper;
import com.digsol.main.location.LocationRepository;
import com.digsol.main.rating.LikeService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminEventServiceImpl implements AdminEventService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EventRepository eventRepository;

    private final StatService statService;

    private final CategoryRepository categoryRepository;

    private final LocationRepository locationRepository;

    private final LikeService likeService;

    @Transactional(readOnly = true)
    @Override
    public Collection<EventFullDto> getAdminEvents(Collection<Long> users,
                                                   Collection<EventState> states,
                                                   Collection<Long> categories,
                                                   String rangeStart,
                                                   String rangeEnd,
                                                   Integer from,
                                                   Integer size) throws ValidationException {

        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, FORMATTER);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, FORMATTER);
        validateDates(start, end);

        Pageable pageable = PageRequest.of(from / size, size);

        Collection<Event> events = eventRepository.findAllOrderByEventDate(users, states, categories, start, end, pageable).toList();

        Map<Long, Integer> views = statService.getViews(events);

        Map<Long, Integer> confirmedRequests = statService.getConfirmedRequests(events);

        Collection<EventFullDto> eventFulls = new ArrayList<>();

        for (Event event : events) {
            Integer viewsCount = views.getOrDefault(event.getId(), 0);

            Integer confirmedRequestsCount = confirmedRequests.getOrDefault(event.getId(), 0);

            Integer rating = likeService.getRating(event.getId());

            rating = null == rating ? 0 : rating;

            EventFullDto eventFullDto = EventMapper.toEventFullDto(
                    event,
                    viewsCount,
                    confirmedRequestsCount,
                    rating
            );

            if (eventFullDto != null) {
                eventFulls.add(eventFullDto);
            }
        }

        log.info("----- {} events found", eventFulls.size());

        return eventFulls;
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = getEvent(eventId);

        LocalDateTime updatedEventDate = updateEventAdminRequest.getEventDate();
        if (updatedEventDate != null) {
            if (updatedEventDate.isBefore(LocalDateTime.now().plusHours(1)))
                throw new AccessDeniedException("You cannot edit events, which start less than in an hour");

            event.setEventDate(updatedEventDate);
        }

        AdminUpdateEventState updatedAdminEventState = updateEventAdminRequest.getStateAction();
        if (updatedAdminEventState != null) {
            if (!event.getState().equals(EventState.PENDING) && updatedAdminEventState.equals(AdminUpdateEventState.PUBLISH_EVENT)) {
                throw new AccessDeniedException("Only events with PENDING request status are allowed for publishing");
            }

            if (event.getState().equals(EventState.PUBLISHED) && updatedAdminEventState.equals(AdminUpdateEventState.REJECT_EVENT)) {
                throw new AccessDeniedException("You cannot reject a published event");
            }

            if (updatedAdminEventState.equals(AdminUpdateEventState.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }

            if (updatedAdminEventState.equals(AdminUpdateEventState.REJECT_EVENT))
                event.setState(EventState.CANCELED);
        }

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }

        if (updateEventAdminRequest.getCategory() != null) {
            event.setCategory(getCategory(updateEventAdminRequest.getCategory()));
        }

        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getLocation() != null) {

            LocationDto updatedLocationDto = updateEventAdminRequest.getLocation();

            Location updatedLocation = findOrCreateLocation(updatedLocationDto);
            event.setLocation(updatedLocation);
        }

        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }

        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }

        if (updateEventAdminRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        }

        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        Map<Long, Integer> views = statService.getViews(List.of(event));

        Map<Long, Integer> confirmedRequests = statService.getConfirmedRequests(List.of(event));

        Integer rating = likeService.getRating(event.getId());

        rating = null == rating ? 0 : rating;

        event = eventRepository.save(event);

        log.info("----- Event {} updated", eventId);

        return EventMapper.toEventFullDto(
                event,
                views.get(event.getId()),
                confirmedRequests.get(event.getId()),
                rating
        );
    }

    private Location findOrCreateLocation(LocationDto locationDto) {

        if (locationDto == null ||
                (locationDto.getLat() == null && locationDto.getLon() == null && locationDto.getName() == null)
        ) {
            return null;
        }
        if (locationDto.getLat() != null && locationDto.getLon() != null) {
            List<Location> locations = new ArrayList<>(
                    findAllLocationsByLatAndLon(locationDto.getLat(), locationDto.getLon()));
            if (locations.isEmpty()) {
                return addLocation(locationDto);
            } else if (locations.size() == 1) {
                return locations.get(0);
            } else if (locationDto.getName() != null) {
                String name = locationDto.getName();
                return locations.stream()
                        .filter(location -> location.getName().equals(name))
                        .findFirst()
                        .orElseGet(() -> addLocation(locationDto));
            }
        }
        return null;
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found"));
    }

    private void validateDates(LocalDateTime start, LocalDateTime end) throws ValidationException {
        if (start != null && end != null && start.isAfter(end)) {
            throw new ValidationException("Start must be before end");
        }
    }

    private Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category " + catId + " not found"));
    }


    private Collection<Location> findAllLocationsByLatAndLon(Double lat, Double lon) {
        return locationRepository.findAllByLatAndLon(lat, lon);
    }

    private Location addLocation(LocationDto locationDto) {
        return locationRepository.save(LocationMapper.toLocation(locationDto));
    }

}
