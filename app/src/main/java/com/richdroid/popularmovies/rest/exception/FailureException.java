package com.richdroid.popularmovies.rest.exception;

class FailureException extends RuntimeException {
    public final int errorCode;

    public FailureException(int errorCode) {
        super("Error code: " + errorCode);
        this.errorCode = errorCode;
    }
}