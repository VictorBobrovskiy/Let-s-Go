package com.digsol.main.stat;

import com.digsol.main.error.NotFoundException;

public class StatNotFoundException extends NotFoundException {
    public StatNotFoundException(String message) {
        super(message);
    }
}