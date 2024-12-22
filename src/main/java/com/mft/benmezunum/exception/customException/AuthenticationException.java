package com.mft.benmezunum.exception.customException;

import com.mft.benmezunum.exception.AllExceptions;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException {

    private final AllExceptions exp;

    public AuthenticationException(AllExceptions allExceptions) {
        super(allExceptions.message);
        this.exp = allExceptions;
    }
    public AuthenticationException(AllExceptions allExceptions, String message) {
        super(message);
        allExceptions.message=message;
        this.exp = allExceptions;
    }

}