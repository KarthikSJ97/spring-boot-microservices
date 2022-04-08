package com.example.todoservice.exceptions;

import com.example.todoservice.dto.ResponseDto;
import com.example.todoservice.dto.UserResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String EXCEPTION = "Exception: {} caused by {} {} {} {}";

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ResponseDto<Object>> handleBadRequestException(BadRequestException ex) {
        List<String> error = new ArrayList<>();
        error.add(ex.getLocalizedMessage());
        ResponseDto<Object> responseDto = ResponseDto.failure(HttpStatus.BAD_REQUEST.value(), "BadRequestException Occurred", error);
        log.error(EXCEPTION, ex.getLocalizedMessage(), ex.getStackTrace()[0], ex.getStackTrace()[1], ex.getStackTrace()[2], ex.getStackTrace()[3]);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    public final ResponseEntity<ResponseDto<Object>> handleFeignException(FeignException ex) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        ResponseDto<UserResponseDto> userResponseDto = objectMapper.readValue(ex.contentUTF8(), new TypeReference<>() {});
        log.error("responseBody: {}", userResponseDto);

        ResponseDto<Object> responseDto = ResponseDto.failure(ex.status(), "FeignException Occurred", userResponseDto.getError());
        log.error(EXCEPTION, ex.getLocalizedMessage(), ex.getStackTrace()[0], ex.getStackTrace()[1], ex.getStackTrace()[2], ex.getStackTrace()[3]);
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(ex.status()));
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
}
