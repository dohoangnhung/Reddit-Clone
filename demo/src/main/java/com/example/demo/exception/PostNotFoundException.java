package com.example.demo.exception;

public class PostNotFoundException extends Throwable {
    public PostNotFoundException(String message) {
        super(message);
    }
}
