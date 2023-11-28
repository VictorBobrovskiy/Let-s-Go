package com.digsol.main.user;

import com.digsol.main.error.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String s) {
        super(s);
    }
}
