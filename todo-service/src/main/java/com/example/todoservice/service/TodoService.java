package com.example.todoservice.service;

import com.example.todoservice.dto.TodoResponseDto;
import com.example.todoservice.entity.Todo;

public interface TodoService {

    TodoResponseDto saveTodo(String userId, Todo todo);

    TodoResponseDto getTodo(String todoId);

    TodoResponseDto updateTodo(String todoId, Todo todo);

    void deleteUser(String todoId);

    void updateTodoStatus(String todoId, boolean isCompleted);
}
