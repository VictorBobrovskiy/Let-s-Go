package com.digsol.main.subscription;

import ru.practicum.user.dto.UserDtoSub;
import ru.practicum.user.dto.UserShortDto;

import java.util.List;

public interface UserSubscriptionService {

    UserDtoSub add(Long userId, Long subsId);

    void delete(Long userId, Long subsId);

    List<UserShortDto> getListSubs(Long userId);
}
