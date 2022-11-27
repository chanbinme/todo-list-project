package toyproject.todo.todo.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class TodoPostDto {
    @NotBlank(message = "내용을 작성해야 합니다.")
    private String title;
}
