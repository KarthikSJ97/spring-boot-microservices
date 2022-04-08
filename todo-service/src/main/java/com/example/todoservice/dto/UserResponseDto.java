package com.example.todoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class UserResponseDto {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String contactNo;
    private String emailId;
}
