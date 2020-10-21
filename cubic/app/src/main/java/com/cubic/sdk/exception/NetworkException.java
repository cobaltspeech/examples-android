package com.cubic.sdk.exception;

public class NetworkException extends Throwable {

    public NetworkException() {
        super("Please check internet connection");
    }
}
