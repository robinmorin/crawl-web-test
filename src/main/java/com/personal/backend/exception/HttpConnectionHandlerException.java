package com.personal.backend.exception;

/***
 * Exception to represent the errors of the Html
 */
public class HttpConnectionHandlerException extends RuntimeException{
    public HttpConnectionHandlerException(String message) {
        super(message);
    }

    public HttpConnectionHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
