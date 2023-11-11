package com.example.demo.exception;

public class SubredditNotFoundException extends Throwable {
    public SubredditNotFoundException(String message) {
        super(message);
    }
}
