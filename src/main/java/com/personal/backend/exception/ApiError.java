package com.personal.backend.exception;

/***
 * Object to represent the error message and status code of the API.
 */
public class ApiError {
    private final Integer status;
    private final String message;

    public ApiError(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
