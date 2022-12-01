package toyproject.todo.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

// 엔티티를 DB에 저장하기 전후에 커스텀 콜백을 요청할 수 있는 어노테이션. 여기서는 AuditingEntityListner.class를 인자로 넘기게 된다.
@EntityListeners(AuditingEntityListener.class)
// 엔티티의 공통 매핑 정보가 필요할 때 주로 사용한다. 즉, 부모 클래스(엔티티)에 필드를 선언하고 단순히 속성만 받아서 사용하고싶을 때 사용하는 방법이다.
@MappedSuperclass
@Getter
public class Auditable {
    @CreatedDate    // 엔티티가 작성된 날짜, created 된 날짜를 사용할 수 있게 해주는 애너테이션
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(updatable = false)
    private LocalDateTime lastModifiedAt;
}
