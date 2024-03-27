package com.personal.backend.dispatch;

import com.personal.backend.utils.SearchCodeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * This class is responsible for handling exceptions that occur during the execution of the search code in any thread.
 */
public class SearchExceptionHandler implements Thread.UncaughtExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(SearchExceptionHandler.class);

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        log.info("Stacktrace: {}", Arrays.toString(ex.getStackTrace()));
        log.info("Exception ocurrs executing Search code: {}. Message: {}. Cause: {}", SearchCodeUtils.getCode(thread.getName()), ex.getMessage(), ex.getCause() != null ? ex.getCause().getMessage() : "No cause");
        thread.interrupt();
    }
}
