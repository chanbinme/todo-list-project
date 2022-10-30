package toyproject.todo.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoPatchDto {
    private long todoId;
    private String content;
}
