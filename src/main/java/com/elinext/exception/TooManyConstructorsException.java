package com.elinext.exception;

public class TooManyConstructorsException extends Exception{
    public TooManyConstructorsException() {
    }

    public TooManyConstructorsException(String message) {
        super(message);
    }

    public TooManyConstructorsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyConstructorsException(Throwable cause) {
        super(cause);
    }
}
