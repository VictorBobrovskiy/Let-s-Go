package com.digsol.main.subscription;

import com.digsol.main.event.EventFullDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final UserSubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionDto subscribeToUser(@RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("----- Subscription creation request received");
        return subscriptionService.subscribe(subscriptionDto);
    }

    @PatchMapping("/unsubscribe")
    public SubscriptionDto unSubscribeToUser(@RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("----- Subscription creation request received");
        return subscriptionService.unSubscribe(subscriptionDto);
    }

    @GetMapping("/search/initiator")
    public List<EventFullDto> getEventsByInitiatorId(@RequestParam Long subId, @RequestParam Long initId,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) String text,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.debug("Private: getting a subscriptions list by initiator id");
        PrivateSearchSubsParams params = PrivateSearchSubsParams.builder()
                .subId(subId)
                .initId(initId)
                .from(from)
                .size(size)
                .categories(categories)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .text(text)
                .sort(sort)
                .build();
        return subscriptionService.getEventsByInitiatorId(params);
    }

    @GetMapping("/search/all")
    public List<EventFullDto> getAllSubscribedEvents(@RequestParam Long subId,
                                                     @RequestParam(required = false) String rangeStart,
                                                     @RequestParam(required = false) String rangeEnd,
                                                     @RequestParam(required = false) String text,
                                                     @RequestParam(required = false) String sort,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) {
        log.debug("Private: getting all subscribed events to user: {}", subId);
        PrivateSearchSubsParams params = PrivateSearchSubsParams.builder()
                .subId(subId)
                .initId(-1)
                .from(from)
                .size(size)
                .categories(categories)
                .rangeEnd(rangeEnd)
                .rangeStart(rangeStart)
                .text(text)
                .sort(sort)
                .build();
        return subscriptionService.getAllSubscribedEvents(params);
    }
}
