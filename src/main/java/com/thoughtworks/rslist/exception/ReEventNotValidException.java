package com.thoughtworks.rslist.exception;

public class ReEventNotValidException extends RuntimeException{
    private String errorMessage;

    public ReEventNotValidException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage(){
        return errorMessage;
    }
}

