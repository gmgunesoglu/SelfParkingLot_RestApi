package com.SoftTech.SelfParkingLot_RestApi.exceptionhandling;

import org.springframework.http.HttpStatus;

public class GlobalRuntimeException extends RuntimeException{
    private final HttpStatus httpStatus;
    private final String message;

    public GlobalRuntimeException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public GlobalResponseEntity toGeneralResponseEntity(){
        return new GlobalResponseEntity(this.httpStatus.value(),this.message,System.currentTimeMillis());
    }
}
