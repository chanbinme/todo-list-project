package toyproject.todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import toyproject.todo.todo.dto.TodoPatchDto;
import toyproject.todo.todo.dto.TodoPostDto;
import toyproject.todo.todo.entity.Todo;
import toyproject.todo.todo.mapper.TodoMapper;
import toyproject.todo.todo.service.TodoService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequestMapping("/v1/todos")
@RestController
public class TodoController {
    private final TodoService todoService;
    private final TodoMapper mapper;

    public TodoController(TodoService todoService, TodoMapper mapper) {
        this.todoService = todoService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postTodo(@Valid @RequestBody TodoPostDto todoPostDto) {
        Todo todo = todoService.createTodo(mapper.todoPostDtoToTodo(todoPostDto));

        return new ResponseEntity<>(mapper.todoToTodoResponseDto(todo), HttpStatus.CREATED);
    }

    @PatchMapping("/{todo-id}")
    public ResponseEntity patchTodo(@PathVariable("todo-id") @Positive long todoID,
            @Valid @RequestBody TodoPatchDto todoPatchDto) {
        todoPatchDto.setTodoId(todoID);
        Todo todo = todoService.updateTodo(mapper.todoPatchDtoToTodo(todoPatchDto));
        return new ResponseEntity<>(mapper.todoToTodoResponseDto(todo), HttpStatus.OK);
    }

    @DeleteMapping("/{todo-id}")
    public ResponseEntity deleteTodo(@PathVariable("todo-id") @Positive long todoId) {
        todoService.deleteTodo(todoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
