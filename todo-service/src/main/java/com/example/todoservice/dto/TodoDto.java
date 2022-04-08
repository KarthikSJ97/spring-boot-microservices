package com.example.todoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class TodoDto {

    @NotBlank(message = "todoTitle cannot be blank")
    private String todoTitle;

    @NotBlank(message = "todoDescription cannot be blank")
    private String todoDescription;

}
