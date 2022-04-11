package com.example.todoservice.http.service;

import com.example.todoservice.dto.ResponseDto;
import com.example.todoservice.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/user/{userId}")
    ResponseDto<UserResponseDto> getUser(@PathVariable String userId);

}
