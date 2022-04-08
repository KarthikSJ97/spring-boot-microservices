package com.example.todoservice.service.impl;

import com.example.todoservice.dto.ResponseDto;
import com.example.todoservice.dto.TodoResponseDto;
import com.example.todoservice.dto.UserResponseDto;
import com.example.todoservice.entity.Todo;
import com.example.todoservice.http.service.UserServiceClient;
import com.example.todoservice.repository.TodoRepository;
import com.example.todoservice.service.TodoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return todoResponseDto;
    }
    
}
