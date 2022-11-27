package toyproject.todo.todo.entity;

import lombok.*;

import javax.persistence.*;
import java.lang.reflect.Member;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 확인 필요
    private Long todoId;

    @Column(length = 30)
    private String title;

    @Column(name = "orders")
    private Long order = 0L;

    private boolean completed = false;

    // Auditable 추가 예정

}
