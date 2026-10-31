package org.blezant.perfumeecommerceapp.exceptions;


public class CustomBadRequestException extends RuntimeException {
    public CustomBadRequestException(String message) {
        super(message);
    }
}
