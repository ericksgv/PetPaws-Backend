package com.example.whiskervet.controller.errorcontroller;

public class CustomException extends RuntimeException {
    private String errorCode;
    private Object errorDetails;

    public CustomException(String errorCode, Object errorDetails) {
        this.errorCode = errorCode;
        this.errorDetails = errorDetails;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object getErrorDetails() {
        return errorDetails;
    }
}
