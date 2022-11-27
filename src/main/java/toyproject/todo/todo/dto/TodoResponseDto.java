package toyproject.todo.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoResponseDto {
    private Long todoId;
    private String title;
    private boolean completed;
    private Long order;
}
