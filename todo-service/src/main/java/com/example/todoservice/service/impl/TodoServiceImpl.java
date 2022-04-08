package com.example.todoservice.service.impl;

import com.example.todoservice.dto.ResponseDto;
import com.example.todoservice.dto.TodoResponseDto;
import com.example.todoservice.dto.UserResponseDto;
import com.example.todoservice.entity.Todo;
import com.example.todoservice.exceptions.BadRequestException;
import com.example.todoservice.exceptions.NotFoundException;
import com.example.todoservice.http.service.UserServiceClient;
import com.example.todoservice.repository.TodoRepository;
import com.example.todoservice.service.TodoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class TodoServiceImpl implements TodoService {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public TodoResponseDto saveTodo(String userId, Todo todo) {
        ResponseDto<UserResponseDto> userResponseDto = userServiceClient.getUser(userId);
        log.info("User details fetched successfully: {}", userResponseDto.getPayload());
        todo.setUserId(userResponseDto.getPayload().getUserId());
        todoRepository.save(todo);
        TodoResponseDto todoResponseDto = new TodoResponseDto();
        BeanUtils.copyProperties(todo, todoResponseDto);
        log.info("Successfully saved the TODO for userId: {}", userId);
        return todoResponseDto;
    }

    @Override
    public TodoResponseDto getTodo(String todoId) {
        Todo todo = todoRepository.findById(convertStringToUUID(todoId))
                .orElseThrow(() -> {
                    log.error(buildTodoDoesNotExistsExceptionString(todoId));
                    throw new NotFoundException(buildTodoDoesNotExistsExceptionString(todoId));
                });
        TodoResponseDto todoResponseDto = new TodoResponseDto();
        BeanUtils.copyProperties(todo, todoResponseDto);
        log.info("Successfully fetched TODO details with todoId: {}", todoId);
        return todoResponseDto;
    }

    private UUID convertStringToUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch(IllegalArgumentException ex) {
            log.error("Invalid UUID string: {}", uuidString);
            throw new BadRequestException("Invalid UUID string: "+uuidString);
        }
    }

    private String buildTodoDoesNotExistsExceptionString(String todoId) {
        return "TODO with todoId: "+todoId+" does not exist";
    }

    @Override
    public TodoResponseDto updateTodo(String todoId, Todo todo) {
        UUID todoIdUuid = convertStringToUUID(todoId);
        Optional<Todo> optionalTodo = todoRepository.findById(todoIdUuid);
        if(optionalTodo.isEmpty()) {
            log.error(buildTodoDoesNotExistsExceptionString(todoId));
            throw new NotFoundException(buildTodoDoesNotExistsExceptionString(todoId));
        }
        todo.setTodoId(todoIdUuid);
        todo.setCompleted(optionalTodo.get().isCompleted());
        todo.setUserId(optionalTodo.get().getUserId());
        todoRepository.save(todo);
        TodoResponseDto todoResponseDto = new TodoResponseDto();
        BeanUtils.copyProperties(todo, todoResponseDto);
        log.info("Successfully updated TODO details for todoId: {}", todoId);
        return todoResponseDto;
    }

    @Override
    public void deleteUser(String todoId) {
        try {
            todoRepository.deleteById(convertStringToUUID(todoId));
            log.info("Successfully deleted TODO details with todoId: {}", todoId);
        } catch(EmptyResultDataAccessException ex) {
            log.error(buildTodoDoesNotExistsExceptionString(todoId));
            throw new NotFoundException(buildTodoDoesNotExistsExceptionString(todoId));
        }
    }

    @Override
    public void updateTodoStatus(String todoId, boolean isCompleted) {
        Todo todo = todoRepository.findById(convertStringToUUID(todoId))
                .orElseThrow(() -> {
                    log.error(buildTodoDoesNotExistsExceptionString(todoId));
                    throw new NotFoundException(buildTodoDoesNotExistsExceptionString(todoId));
                });
        if(todo.isCompleted() == isCompleted) {
            log.error("TODO already in the requested status");
            throw new BadRequestException("TODO already in the requested status");
        }
        todo.setCompleted(isCompleted);
        todoRepository.save(todo);
        log.info("Successfully update the TODO status to {}", isCompleted);
    }

}
