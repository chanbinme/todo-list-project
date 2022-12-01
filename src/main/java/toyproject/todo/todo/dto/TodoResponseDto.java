package toyproject.todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TodoResponseDto {
    private Long todoId;
    private String title;
    private Long order;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
