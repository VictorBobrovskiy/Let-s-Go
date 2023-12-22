package com.digsol.main.category;

import com.digsol.main.error.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String s) {
        super(s);
    }
}
