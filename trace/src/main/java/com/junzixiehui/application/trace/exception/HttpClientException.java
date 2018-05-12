package com.junzixiehui.application.trace.exception;

/**
 * <p>Description: </p>
 *
 * @author: by qulibin
 * @date: 2018/5/12  17:56
 * @version: 1.0
 */
public class HttpClientException extends RuntimeException {
    public HttpClientException(String message) {
        super(message);
    }

    public HttpClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
