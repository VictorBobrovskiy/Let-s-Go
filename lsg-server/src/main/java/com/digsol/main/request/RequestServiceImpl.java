package com.digsol.main.request;

import com.digsol.main.error.AccessDeniedException;
import com.digsol.main.error.NotFoundException;
import com.digsol.main.event.EventNotFoundException;
import com.digsol.main.event.EventRepository;
import com.digsol.main.event.EventState;
import com.digsol.main.stat.StatService;
import com.digsol.main.user.User;
import com.digsol.main.user.UserNotFoundException;
import com.digsol.main.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.digsol.main.event.Event;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    private final StatService statService;

    @Transactional(readOnly = true)
    @Override
    public Collection<RequestDto> getRequests(Long userId) {

        Collection<Request> requests = requestRepository.findAllByRequesterId(userId);

        log.info("----- {} requests found", requests.size());

        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);

        if (!requestRepository.findAllByRequesterIdAndEventId(userId, eventId).isEmpty()) {
            throw new AccessDeniedException("Request from user " + userId + " for event " + eventId + " already exists");
        }

        if (userId.equals(event.getInitiator().getId())) {
            throw new AccessDeniedException("Event initiator cannot request to join his own event");
        }

        if (!EventState.PUBLISHED.equals(event.getState())) {
            throw new AccessDeniedException("Event " + eventId + " is not published yet");
        }

        RequestStatus status = RequestStatus.PENDING;
        if (event.getParticipantLimit().equals(0) || Boolean.TRUE.equals(!event.getRequestModeration())) {
            status = RequestStatus.CONFIRMED;
        }

        int confirmedRequests = statService.getConfirmedRequests(List.of(event)).getOrDefault(eventId, 0) + 1;
        if (event.getParticipantLimit() != 0 && confirmedRequests > event.getParticipantLimit()) {
            throw new AccessDeniedException("Participant limit reached for event " + eventId);
        }

        Request request = new Request(user, event, status);

        request = requestRepository.save(request);

        log.info("Request created {}", request.getId());

        return RequestMapper.toRequestDto(request);
    }

    @Override
    public RequestDto cancelRequest(Long userId, Long requestId) {

        getUser(userId);

        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request " + requestId + " not found"));

        if (!request.getRequester().getId().equals(userId)) {
            throw new AccessDeniedException("User " + userId + " cannot cancel request " + requestId);
        }

        if (RequestStatus.CONFIRMED.equals(request.getStatus())) {
            throw new AccessDeniedException("Request " + requestId + " already confirmed");
        }

        request.setStatus(RequestStatus.CANCELED);

        log.info("----- Request {} canceled", request.getId());

        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User " + userId + " not found"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Event " + eventId + " not found"));
    }
}