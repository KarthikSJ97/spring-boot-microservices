package com.example.todoservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
public class TodoResponseDto extends TodoDto {

    private UUID todoId;
    private boolean isCompleted;
}
