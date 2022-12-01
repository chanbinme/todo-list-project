package toyproject.todo.todo.entity;

import lombok.*;
import toyproject.todo.audit.Auditable;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Todo extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 확인 필요
    private Long todoId;

    @Column(length = 30)
    private String title;

    @Column(name = "orders")
    private Long order = 0L;

    private boolean completed = false;
}
