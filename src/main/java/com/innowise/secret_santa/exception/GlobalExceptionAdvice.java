package com.innowise.secret_santa.exception;

import com.innowise.secret_santa.util.CalendarUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<ErrorObject> handleNoDataFoundException(NoDataFoundException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SaveDataException.class)
    public ResponseEntity<ErrorObject> handleAccountCreatedException(SaveDataException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnknownMessageException.class)
    public ResponseEntity<ErrorObject> handleUnknownMessageException(UnknownMessageException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ErrorSendEmailException.class)
    public ResponseEntity<ErrorObject> handleErrorSendEmailException(ErrorSendEmailException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MapperException.class)
    public ResponseEntity<ErrorObject> handleMapperException(MapperException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IncorrectDataException.class)
    public ResponseEntity<ErrorObject> handleIncorrectDataException(IncorrectDataException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<ErrorObject> handleNoAccessException(NoAccessException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.FORBIDDEN.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorObject> handleAuthorizationException(UnauthorizedException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getAllErrors()
                .stream()
                .map(message -> message.getDefaultMessage()+"   ")
                .collect(Collectors.joining()));
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorObject> handleValidationException(ValidationException ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorObject.setMessage(ex.getLocalizedMessage());
        errorObject.setTimestamp(CalendarUtils.getFormatDate(LocalDateTime.now()));

        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }
}