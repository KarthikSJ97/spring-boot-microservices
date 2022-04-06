package com.example.userservice.exceptions;

import com.example.userservice.dto.ResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
}
