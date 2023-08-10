package com.SoftTech.SelfParkingLot_RestApi.exceptionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {




    @ExceptionHandler(GlobalRuntimeException.class)
    public ResponseEntity<GlobalResponseEntity> handleCustomException(GlobalRuntimeException exception) {
        GlobalResponseEntity errorResponse = exception.toGeneralResponseEntity();
        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<GlobalResponseEntity> handleException (Exception exception){
        GlobalResponseEntity error = new GlobalResponseEntity();
        error.setMessage(exception.getMessage());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }
}
