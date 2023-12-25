package com.digsol.main.event;

import com.digsol.main.user.User;
import com.digsol.main.user.UserMapper;
import com.digsol.main.user.UserShortDto;
import lombok.experimental.UtilityClass;
import com.digsol.main.category.Category;
import com.digsol.main.category.CategoryDto;
import com.digsol.main.category.CategoryMapper;
import com.digsol.main.location.Location;
import com.digsol.main.location.LocationMapper;

@UtilityClass
public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, User initiator, Category category, Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(initiator)
                .location(location)
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event,
                                              Integer views,
                                              Integer confirmedRequests,
                                              Integer rate) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .rate(rate)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event,
                                                CategoryDto categoryDto,
                                                UserShortDto initiator,
                                                Integer views,
                                                Integer confirmedRequests,
                                                Integer rate) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDto)
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(initiator)
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .rate(rate)
                .build();
    }
}