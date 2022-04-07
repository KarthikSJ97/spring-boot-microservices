package com.example.userservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
public class UserDto {

    @NotBlank(message = "firstName cannot be blank")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank")
    private String lastName;

    @NotNull(message = "contactNo cannot be blank")
    @Size(min = 10, max = 10, message = "contactNo should be exactly 10 digits long")
    private String contactNo;

    @NotBlank(message = "emailId cannot be blank")
    private String emailId;
}
