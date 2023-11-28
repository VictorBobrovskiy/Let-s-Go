package com.digsol.main.rating;

import com.digsol.main.error.NotFoundException;

public class LikeNotFoundException extends NotFoundException {
    public LikeNotFoundException(String s) {
        super(s);
    }
}
