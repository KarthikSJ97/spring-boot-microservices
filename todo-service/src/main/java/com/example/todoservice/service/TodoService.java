package com.example.todoservice.service;

import com.example.todoservice.dto.TodoResponseDto;
import com.example.todoservice.entity.Todo;

public interface TodoService {

    TodoResponseDto saveTodo(String userId, Todo todo);

    TodoResponseDto getTodo(String todoId);
}
