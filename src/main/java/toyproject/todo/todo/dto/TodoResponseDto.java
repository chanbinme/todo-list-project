package toyproject.todo.todo.dto;

import lombok.Getter;
import lombok.Setter;
import toyproject.todo.todo.entity.Todo.TodoStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class TodoResponseDto {

    private long todoId;
    private String content;
    private LocalDateTime createdAt;
    private TodoStatus todoStatus;
}
