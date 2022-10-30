package toyproject.todo.todo.service;

import org.springframework.stereotype.Service;
import toyproject.todo.todo.entity.Todo;
import toyproject.todo.todo.repository.TodoRepository;

import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo createTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
//        Todo findTodo = todoRepository.findById(todo.getTodoId());
        return todoRepository.save(todo);
    }

    public void deleteTodo(long todoId) {
        todoRepository.deleteById(todoId);
    }
}
