package com.example.todoservice.controller;

import com.example.todoservice.dto.ResponseDto;
import com.example.todoservice.dto.TodoResponseDto;
import com.example.todoservice.dto.TodoDto;
import com.example.todoservice.entity.Todo;
import com.example.todoservice.service.TodoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/todo")
@Log4j2
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity<ResponseDto<TodoResponseDto>> saveTodo(@RequestParam String userId, @RequestBody @Valid TodoDto todoDto) {
        log.info("Received a request to save a TODO: {} for userId: {}", todoDto, userId);
        Todo todo = new Todo();
        BeanUtils.copyProperties(todoDto, todo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDto.success(HttpStatus.CREATED.value(), "Successfully saved a TODO", todoService.saveTodo(userId, todo)));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ResponseDto<TodoResponseDto>> getTodo(@PathVariable String todoId) {
        log.info("Received a request to fetch TODO details for todoId: {}", todoId);
        return ResponseEntity.ok()
                .body(ResponseDto.success(HttpStatus.OK.value(), "Successfully fetched the TODO details", todoService.getTodo(todoId)));
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<ResponseDto<TodoResponseDto>> updateTodo(@PathVariable String todoId, @RequestBody @Valid TodoDto todoDto) {
        log.info("Received a request to update TODO details for todoId: {}", todoId);
        Todo todo = new Todo();
        BeanUtils.copyProperties(todoDto, todo);
        return ResponseEntity.ok()
                .body(ResponseDto.success(HttpStatus.OK.value(), "Successfully updated the TODO details", todoService.updateTodo(todoId, todo)));
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String todoId) {
        log.info("Received a request to delete a TODO for todoId: {}", todoId);
        todoService.deleteUser(todoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{todoId}/isCompleted/{isCompleted}")
    public ResponseEntity<Void> updateTodoStatus(@PathVariable String todoId, @PathVariable boolean isCompleted) {
        log.info("Received a request to update the status of the TODO with todoId: {} to {}", todoId, isCompleted);
        todoService.updateTodoStatus(todoId, isCompleted);
        return ResponseEntity.noContent().build();
    }

}
