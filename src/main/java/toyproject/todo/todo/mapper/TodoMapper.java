package toyproject.todo.todo.mapper;

import org.mapstruct.Mapper;
import toyproject.todo.todo.dto.TodoPatchDto;
import toyproject.todo.todo.dto.TodoPostDto;
import toyproject.todo.todo.dto.TodoResponseDto;
import toyproject.todo.todo.entity.Todo;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo todoPostToTodo(TodoPostDto todoPostDto);

    Todo todoPatchToTodo(TodoPatchDto todoPatchDto);

    TodoResponseDto todoToTodoResponse(Todo todo);

    List<TodoResponseDto> todosToTodoResponses(List<Todo> todos);
}
