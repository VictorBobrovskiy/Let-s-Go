package com.digsol.main.event;

import com.digsol.main.error.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(String s) {
        super(s);
    }
}
