package com.digsol.main.user;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserRequestDTO userRequestDTO) {
        return new User(
                userRequestDTO.getName(),
                userRequestDTO.getEmail()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}