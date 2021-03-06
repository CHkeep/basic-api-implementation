package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.ReEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RsEventHandler {
    @ExceptionHandler({ReEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandlerMain(Exception e){
        String errorMessage;
        if(e instanceof MethodArgumentNotValidException){
            errorMessage = "invalid user";
        }else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
