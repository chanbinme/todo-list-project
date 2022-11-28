package toyproject.todo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class TodoPostDto {
    @NotBlank(message = "내용을 작성해야 합니다.")
    private String title;

    @NotNull
    private Long order;

    @NotNull
    private Boolean completed;
}
