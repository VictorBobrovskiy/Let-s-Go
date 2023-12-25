package com.digsol.main.user;

import java.util.Collection;

public interface AdminUsersService {
    Collection<UserDto> getUsers(Collection<Long> ids, Integer from, Integer size);

    User getUser(Long userId);

    UserDto addUser(UserRequestDTO userRequestDTO);

    void deleteUser(Long catId);
}
