package toyproject.todo.todo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@Setter
public class Todo {

    @Id
    private long todoId;
    private String content;
    private TodoStatus todoStatus = TodoStatus.TODO_ACTIVE;
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum TodoStatus {
        TODO_ACTIVE(1, "Active"),
        TODO_COMPLETE(2, "Completed");

        @Getter
        private int stepNumb;

        @Getter
        private String stepDescription;

        TodoStatus(int stepNumb, String stepDescription) {
            this.stepNumb = stepNumb;
            this.stepDescription = stepDescription;
        }
    }

}
