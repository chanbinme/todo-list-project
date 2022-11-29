package toyproject.todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TodoResponseDto {
    private Long todoId;
    private String title;
    private Long order;
    private boolean completed;
}
