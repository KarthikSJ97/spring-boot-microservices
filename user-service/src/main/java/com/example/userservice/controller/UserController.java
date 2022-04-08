package com.example.userservice.controller;

import com.example.userservice.dto.ResponseDto;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserResponseDto;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Log4j2
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<ResponseDto<UserResponseDto>> saveUser(@RequestBody @Valid UserDto userDto) {
        log.info("Received a request to save a user: {}", userDto);
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(HttpStatus.CREATED.value(), "Successfully saved the user", userService.saveUser(user)));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserResponseDto>> getUser(@PathVariable String userId) {
        log.info("Received a request to fetch a user details for userId: {}", userId);
        return ResponseEntity.ok()
                .body(ResponseDto.success(HttpStatus.OK.value(), "Successfully fetched the user", userService.getUser(userId)));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ResponseDto<UserResponseDto>> updateUser(@PathVariable String userId, @RequestBody @Valid UserDto userDto) {
        log.info("Received a request to update a user details for userId: {}", userId);
        User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return ResponseEntity.ok()
                .body(ResponseDto.success(HttpStatus.OK.value(), "Successfully updated the user", userService.updateUser(userId, user)));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        log.info("Received a request to delete a user details for userId: {}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
