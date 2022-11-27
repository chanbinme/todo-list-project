package toyproject.todo.todo.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TodoPatchDto {

    private Long todoId;

    @NotBlank(message = "내용이 공백이 아니어야 합니다.")
    private String title;

    private boolean completed;
}
