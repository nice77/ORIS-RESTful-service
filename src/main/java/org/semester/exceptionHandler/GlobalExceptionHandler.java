package org.semester.exceptionHandler;


import org.semester.exceptionHandler.exception.BadTokenException;
import org.semester.exceptionHandler.exception.WrongCredentialsException;
import org.semester.util.StaticString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadTokenException.class)
    public ResponseEntity<String> tokenRevokedExceptionHandler(Exception ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<String> wrongCredentialsExceptionHandler(Exception ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> exceptionHandler(Exception ex){
        return switch (StaticString.getByValue(ex.getMessage())) {
            case ERROR_ON_FILE_ADD, ERROR_ON_FILE_READ, ERROR_ON_FILE_DELETE, EVENT_NOT_FOUND, USER_NOT_FOUND, TOKEN_NOT_FOUND ->
                    ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            case WRONG_FILE_TYPE -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
            case WRONG_CREDENTIALS, EMAIL_IN_USE, BANNED ->
                    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        };
    }
}
