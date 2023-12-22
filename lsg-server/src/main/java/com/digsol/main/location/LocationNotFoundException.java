package com.digsol.main.location;

import com.digsol.main.error.NotFoundException;

public class LocationNotFoundException extends NotFoundException {

    public LocationNotFoundException(String s) {
        super(s);
    }
}
