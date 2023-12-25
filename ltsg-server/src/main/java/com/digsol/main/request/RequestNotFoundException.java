package com.digsol.main.request;

import com.digsol.main.error.NotFoundException;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(String s) {
        super(s);
    }
}
