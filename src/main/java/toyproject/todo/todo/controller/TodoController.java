package toyproject.todo.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoMapper mapper;

    @PostMapping
    public ResponseEntity createTodo(@Valid @RequestBody TodoPostDto todoPostDto) {
        Todo todo = todoService.saveTodo(mapper.todoPostToTodo(todoPostDto));

        return new ResponseEntity<>(mapper.todoToTodoResponse(todo), HttpStatus.CREATED);
    }

    @GetMapping("/{todoId}")
    public ResponseEntity findTodo(@PathVariable("todoId") Long todoID) {
        Todo findTodo = todoService.findTodo(todoID);

        return new ResponseEntity<>(mapper.todoToTodoResponse(findTodo), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity findTodos(@RequestParam @Positive int page,
                                    @RequestParam @Positive int size) {
        Page<Todo> pageTodos = todoService.findTodos(page, size);
        List<Todo> todos = pageTodos.getContent();

        return new ResponseEntity<>(mapper.todosToTodoResponses(todos), HttpStatus.OK);
    }

    @PatchMapping("/{todoId}")
    public ResponseEntity updateTodo(@PathVariable @Positive Long todoId,
                                     @Valid @RequestBody TodoPatchDto todoDtoPatch) {
        todoDtoPatch.setTodoId(todoId);
        Todo todo = todoService.updateTodo(mapper.todoPatchToTodo(todoDtoPatch));

        return new ResponseEntity<>(mapper.todoToTodoResponse(todo), HttpStatus.OK);
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity deleteTodo(@PathVariable @Positive Long todoId) {
        todoService.deleteTodo(todoId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
