package com.digsol.main.user;

import com.digsol.main.event.*;
import com.digsol.main.request.*;
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
import com.digsol.main.error.AccessDeniedException;
import ru.practicum.main.event.*;
import com.digsol.main.location.Location;
import com.digsol.main.location.LocationService;
import com.digsol.main.rating.LikeService;
import ru.practicum.main.request.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEventServiceImpl implements UserEventService {

    private final EventRepository eventRepository;

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final StatService statService;

    private final CategoryRepository categoryRepository;

    private final LocationService locationService;

    private final EventService eventService;


    private final LikeService likeService;


    @Override
    public Event addEvent(Long userId, NewEventDto newEventDto) {

        User user = getUser(userId);

        Category category = getCategory(newEventDto.getCategory());

        Location location = locationService.findOrCreateLocation(newEventDto.getLocation());

        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, user, category, location));

        log.info("----- Event {} saved", event.getId());

        return event;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found."));

        Map<Long, Integer> views = statService.getViews(List.of(event));

        Map<Long, Integer> confirmedRequests = statService.getConfirmedRequests(List.of(event));

        Integer rating = likeService.getRating(eventId);

        log.info("Event Requested {}", eventId);

        return EventMapper.toEventFullDto(
                event,
                views.get(event.getId()),
                confirmedRequests.get(event.getId()),
                rating
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size) {

        getUser(userId);

        Pageable pageable = PageRequest.of(from / size, size);

        Collection<Event> events = eventRepository.findAllByInitiatorId(userId, pageable).toList();

        log.debug("----- Found {} events", events.size());

        return eventService.mapToEventShorts(events);
    }

    @Override
    public EventRequestStatusUpdateResult updateUserEventRequest(Long userId,
                                                                 Long eventId,
                                                                 EventRequestStatusUpdateRequest
                                                                         eventRequestStatusUpdateRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException("User " + userId + " is not an owner of the event " + eventId);
        }

        int confirmedRequests = statService.getConfirmedRequests(List.of(event)).getOrDefault(eventId, 0) + 1;

        if (event.getParticipantLimit() != 0 && confirmedRequests > event.getParticipantLimit()) {
            throw new AccessDeniedException("Participant limit reached for event " + eventId);
        }

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();

        eventRequestStatusUpdateRequest.getRequestIds().forEach(requestId -> {
            Request request = requestRepository.findById(requestId)
                    .orElseThrow(() -> new RequestNotFoundException("Request " + requestId + " not found"));

            if (!RequestStatus.PENDING.equals(request.getStatus())) {
                throw new AccessDeniedException("PENDING request status required");
            }

            EventUpdateRequestStatus updateStatus = eventRequestStatusUpdateRequest.getStatus();

            if (EventUpdateRequestStatus.CONFIRMED.equals(updateStatus)) {
                request.setStatus(RequestStatus.CONFIRMED);
                eventRequestStatusUpdateResult.getConfirmedRequests().add(RequestMapper.toRequestDto(request));
            } else if (EventUpdateRequestStatus.REJECTED.equals(updateStatus)) {
                request.setStatus(RequestStatus.REJECTED);
                eventRequestStatusUpdateResult.getRejectedRequests().add(RequestMapper.toRequestDto(request));
            }

            requestRepository.save(request);
        });

        log.info("----- Request status updated for the event {}", eventId);

        return eventRequestStatusUpdateResult;
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        getUser(userId);

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException("User " + userId + " is not an owner of the event " + eventId);
        }

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new AccessDeniedException("You cannot update a confirmed event");
        }

        LocalDateTime updatedEventDate = updateEventUserRequest.getEventDate();
        if (updatedEventDate != null) {
            if (updatedEventDate.isBefore(LocalDateTime.now().plusHours(2)))
                throw new AccessDeniedException("You cannot update events, which start less than in two hours");

            event.setEventDate(updatedEventDate);
        }

        UserUpdateEventStatus updatedUserEventState = updateEventUserRequest.getStateAction();

        if (updatedUserEventState != null) {

            if (updatedUserEventState.equals(UserUpdateEventStatus.SEND_TO_REVIEW))
                event.setState(EventState.PENDING);

            if (updatedUserEventState.equals(UserUpdateEventStatus.CANCEL_REVIEW))
                event.setState(EventState.CANCELED);
        }

        if (updateEventUserRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequest.getAnnotation());
        }

        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(getCategory(updateEventUserRequest.getCategory()));
        }

        if (updateEventUserRequest.getDescription() != null) {
            event.setDescription(updateEventUserRequest.getDescription());
        }

        if (updateEventUserRequest.getLocation() != null) {
            Location updatedLocation = locationService.findOrCreateLocation(updateEventUserRequest.getLocation());
            event.setLocation(updatedLocation);
        }

        if (updateEventUserRequest.getPaid() != null) {
            event.setPaid(updateEventUserRequest.getPaid());
        }

        if (updateEventUserRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
        }

        if (updateEventUserRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequest.getRequestModeration());
        }

        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        Map<Long, Integer> views = statService.getViews(List.of(event));

        Map<Long, Integer> confirmedRequests = statService.getConfirmedRequests(List.of(event));

        Integer rating = likeService.getRating(eventId);

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

    @Transactional(readOnly = true)
    @Override
    public Collection<RequestDto> getUserEventRequests(Long userId, Long eventId) {
        getUser(userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new AccessDeniedException("User " + userId + " is not an owner of the event " + eventId);
        }

        Collection<Request> requests = requestRepository.findAllByEventId(eventId);

        log.info("----- {} requests found", requests.size());

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " not found"));
    }

    private Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new CategoryNotFoundException("Category " + catId + " not found"));
    }

}
