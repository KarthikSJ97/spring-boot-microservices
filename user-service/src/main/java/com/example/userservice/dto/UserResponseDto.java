package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
public class UserResponseDto extends UserDto {

    private UUID userId;
}
