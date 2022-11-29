package toyproject.todo.todo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import toyproject.todo.todo.entity.Todo;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void saveTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);

        // when
        Todo saveTodo = todoRepository.save(todo);

        // then
        assertNotNull(saveTodo);
        assertEquals(todo.getTodoId(), saveTodo.getTodoId());
        assertEquals(todo.getTitle(), saveTodo.getTitle());
        assertEquals(todo.getOrder(), saveTodo.getOrder());
        assertEquals(todo.isCompleted(), saveTodo.isCompleted());
    }

    @Test
    public void findByTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);

        // when
        todoRepository.save(todo);
        Todo findTodo = todoRepository.findByOrder(todo.getOrder()).get();

        // then
        assertNotNull(findTodo);
        assertEquals(todo.getTitle(), findTodo.getTitle());
        assertEquals(todo.getOrder(), findTodo.getOrder());
        assertEquals(todo.isCompleted(), findTodo.isCompleted());
    }

    @Test
    public void findByTodosTest() throws Exception {
        // given
        Todo todo1 = new Todo(1L, "양치하기", 1L, false);
        Todo todo2 = new Todo(2L, "세수하기", 2L, false);

        // when
        todoRepository.save(todo1);
        todoRepository.save(todo2);

        List<Todo> todos = todoRepository.findAll();

        // then
        assertNotNull(todos);
        assertEquals(todos.size(), 2);
        assertEquals(todo1.getTitle(), todos.get(0).getTitle());
        assertEquals(todo2.getTitle(), todos.get(1).getTitle());
    }

    @Test
    public void updateTodoTest() throws Exception {
        // given
        Todo todo1 = new Todo(1L, "양치하기", 1L, false);
        Todo todo2 = new Todo(1L, "세수하기", 1L, true);

        // when
        todoRepository.save(todo1);
        Todo updateTodo = todoRepository.save(todo2);

        // then
        assertNotNull(updateTodo);
        assertEquals(todo2.getTitle(), updateTodo.getTitle());
        assertEquals(todo2.isCompleted(), updateTodo.isCompleted());
        assertNotEquals(todo1.getTitle(), updateTodo.getTitle());
        assertNotEquals(todo1.isCompleted(), updateTodo.isCompleted());
    }

    @Test
    public void deleteTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);

        // when
        Todo saveTodo = todoRepository.save(todo);
        todoRepository.delete(saveTodo);

        // then
        assertThrows(NoSuchElementException.class, () -> todoRepository.findById(saveTodo.getTodoId()).get());
        assertTrue(todoRepository.findById(saveTodo.getTodoId()).isEmpty());
    }

}