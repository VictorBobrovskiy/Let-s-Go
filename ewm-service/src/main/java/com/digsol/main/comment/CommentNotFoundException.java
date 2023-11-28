package com.digsol.main.comment;

import com.digsol.main.error.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    public CommentNotFoundException(String s) {
        super(s);
    }
}
