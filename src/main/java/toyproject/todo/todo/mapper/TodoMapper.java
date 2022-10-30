package toyproject.todo.todo.mapper;


import org.mapstruct.Mapper;
import toyproject.todo.todo.dto.TodoPatchDto;
import toyproject.todo.todo.dto.TodoPostDto;
import toyproject.todo.todo.dto.TodoResponseDto;
import toyproject.todo.todo.entity.Todo;

@Mapper(componentModel = "spring")
public interface TodoMapper {
    Todo todoPostDtoToTodo(TodoPostDto todoPostDto);
    Todo todoPatchDtoToTodo(TodoPatchDto todoPatchDto);
    TodoResponseDto todoToTodoResponseDto(Todo todo);
}
