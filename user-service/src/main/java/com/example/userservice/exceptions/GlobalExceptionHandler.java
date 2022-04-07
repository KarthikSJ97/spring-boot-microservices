package com.example.userservice.exceptions;

import com.example.userservice.dto.ResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String EXCEPTION = "Exception: {} caused by {} {} {} {}";

    @ExceptionHandler(DuplicateResourceException.class)
    public final ResponseEntity<ResponseDto<Object>> handleDuplicateResourceException(DuplicateResourceException ex) {
        List<String> error = new ArrayList<>();
        error.add(ex.getLocalizedMessage());
        ResponseDto<Object> responseDto = ResponseDto.failure(HttpStatus.CONFLICT.value(), "DuplicateResourceException Occurred", error);
        log.error(EXCEPTION, ex.getLocalizedMessage(), ex.getStackTrace()[0], ex.getStackTrace()[1], ex.getStackTrace()[2], ex.getStackTrace()[3]);
        return new ResponseEntity<>(responseDto, HttpStatus.CONFLICT);
    }

    @Override
    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        List<String> errors = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            errors.add(error.getDefaultMessage());
        }
        ResponseDto<Object> responseDto = ResponseDto.failure(HttpStatus.BAD_REQUEST.value(), "Validation Failed", errors);
        log.error(EXCEPTION, ex.getLocalizedMessage(), ex.getStackTrace()[0], ex.getStackTrace()[1], ex.getStackTrace()[2], ex.getStackTrace()[3]);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ResponseDto<Object>> handleNotFoundException(NotFoundException ex) {
        List<String> error = new ArrayList<>();
        error.add(ex.getLocalizedMessage());
        ResponseDto<Object> responseDto = ResponseDto.failure(HttpStatus.NOT_FOUND.value(), "NotFoundException Occurred", error);
        log.error(EXCEPTION, ex.getLocalizedMessage(), ex.getStackTrace()[0], ex.getStackTrace()[1], ex.getStackTrace()[2], ex.getStackTrace()[3]);
        return new ResponseEntity<>(responseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ResponseDto<Object>> handleBadRequestException(BadRequestException ex) {
        List<String> error = new ArrayList<>();
        error.add(ex.getLocalizedMessage());
        ResponseDto<Object> responseDto = ResponseDto.failure(HttpStatus.BAD_REQUEST.value(), "BadRequestException Occurred", error);
        log.error(EXCEPTION, ex.getLocalizedMessage(), ex.getStackTrace()[0], ex.getStackTrace()[1], ex.getStackTrace()[2], ex.getStackTrace()[3]);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
