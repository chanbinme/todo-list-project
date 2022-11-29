package toyproject.todo.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import toyproject.todo.todo.entity.Todo;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, CrudRepository<Todo, Long> {
    Optional<Todo> findByOrder(Long order);
}
