package com.example.todoservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ResponseDto<T> {

    private LocalDateTime timestamp = LocalDateTime.now();

    private int status;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T payload;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String path = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
            .getRequest().getRequestURI();

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> error;

    public ResponseDto(int status, String message, T payload) {
        this.status = status;
        this.message = message;
        this.payload = payload;
    }

    public ResponseDto(int status, String message, List<String> error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public static <T> ResponseDto<T> success(int status, String message, T payload) {
        return new ResponseDto<>(status, message, payload);
    }

    public static <T> ResponseDto<T> failure(int status, String message, List<String> error) {
        return new ResponseDto<>(status, message, error);
    }
}
