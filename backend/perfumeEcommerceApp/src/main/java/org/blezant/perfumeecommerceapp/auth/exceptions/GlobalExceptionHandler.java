package org.blezant.perfumeecommerceapp.auth.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> fieldMethodExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("status" , false);
        errors.put("error",HttpStatus.BAD_REQUEST.toString());
        exception.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<?> fieldMethodExceptionHandler(CustomBadRequestException customBadRequestException) {
        ErrorResponse errorResponse= new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage(customBadRequestException.getMessage());
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> usernameNotFoundExceptionHandler(UsernameNotFoundException usernameNotFoundException){
        ErrorResponse errorResponse= new ErrorResponse();
        errorResponse.setStatus(false);
        errorResponse.setMessage(usernameNotFoundException.getMessage());
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        return ResponseEntity.badRequest().body(errorResponse);
    }

}
