package com.raydovski.simpleblogging.exceptions;

public class PostNotFoundException extends EntityNotFoundException {

    /**
     * 
     */
    private static final long serialVersionUID = -1559508562929189753L;

    public PostNotFoundException() {
        super("Post not found");
    }

}