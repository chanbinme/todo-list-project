package toyproject.todo.todo.repository;

import org.springframework.data.repository.CrudRepository;
import toyproject.todo.todo.entity.Todo;

// interface끼리는 extends 사용
public interface TodoRepository extends CrudRepository<Todo, Long> {
}
