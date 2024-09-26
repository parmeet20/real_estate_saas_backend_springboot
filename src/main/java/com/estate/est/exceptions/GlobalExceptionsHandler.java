package com.estate.est.exceptions;

import com.estate.est.dto.ErrorDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionsHandler {
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userExceptions(UserException ex){
        return new ResponseEntity<>(new ErrorDetail(ex.getMessage(),false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorDetail> chatExceptions(ChatException ex) {
        return new ResponseEntity<>(new ErrorDetail(ex.getMessage(), false), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail>invalidArgumentExc(MethodArgumentNotValidException ex){
        return new ResponseEntity<>(new ErrorDetail(ex.getMessage(),false),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PropertyException.class)
    public ResponseEntity<ErrorDetail>propertiesExc(PropertyException ex){
        return new ResponseEntity<>(new ErrorDetail(ex.getMessage(),false),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MailExceptions.class)
    public ResponseEntity<ErrorDetail>mailExceptions(MailExceptions ex){
        return new ResponseEntity<>(new ErrorDetail(ex.getMessage(),false),HttpStatus.NOT_FOUND);
    }
}
