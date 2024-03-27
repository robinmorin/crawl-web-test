package com.personal.backend.exception;

/***
 * Exception to represent the errors of the HtmlDocUtils class.
 */
public class HtmlDocUtilsException extends RuntimeException{
    public HtmlDocUtilsException(String message) {
        super(message);
    }

    public HtmlDocUtilsException(String message, Throwable cause) {
        super(message, cause);
    }
}
