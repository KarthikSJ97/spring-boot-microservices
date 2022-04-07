package com.example.userservice.service;

import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;

public interface UserService {

    UserResponseDto saveUser(User user);

    UserResponseDto getUser(String userId);
}
