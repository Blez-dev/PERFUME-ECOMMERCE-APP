package org.blezant.perfumeecommerceapp.auth.exceptions;


public class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String message) {
        super(message);
    }
}
