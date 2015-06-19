package com.vergal.library.dataproxy;

/**
 * Library custom exception.
 */
public final class DataProxyException extends RuntimeException {

    public DataProxyException(final String message) {
        super(message);
    }

    public DataProxyException(final String message, Throwable throwable) {
        super(message, throwable);
    }
}
