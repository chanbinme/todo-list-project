package toyproject.todo.todo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import toyproject.todo.todo.dto.TodoPatchDto;
import toyproject.todo.todo.entity.Todo;
import toyproject.todo.todo.repository.TodoRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    public void createTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);

        given(todoRepository.save(Mockito.any(Todo.class))).willReturn(todo);
        given(todoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(todo));

        // when
        Long todoId = todoService.saveTodo(todo).getTodoId();
        Todo findTodo = todoRepository.findById(todoId).get();

        // then
        assertEquals(todo.getTodoId(), findTodo.getTodoId());
        assertEquals(todo.getTitle(), findTodo.getTitle());
        assertEquals(todo.getOrder(), findTodo.getOrder());
        assertEquals(todo.isCompleted(), findTodo.isCompleted());
    }


    @Test
    public void findTodoTest() throws Exception {
        // given
        Todo todo1 = new Todo(1L, "양치하기", 1L, false);
        Todo todo2 = new Todo(2L, "기침하기", 2L, false);

        given(todoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(todo1));
        // when
        Todo findTodo = todoService.findTodo(todo1.getTodoId());

        // then
        assertEquals(todo1.getTodoId(), findTodo.getTodoId());
        assertEquals(todo1.getTitle(), findTodo.getTitle());
        assertEquals(todo1.getOrder(), findTodo.getOrder());
        assertEquals(todo1.isCompleted(), findTodo.isCompleted());
    }

    @Test
    public void verifiedTodoTest() throws Exception {
        // given
        given(todoRepository.findById(Mockito.anyLong())).willReturn(null);

        // when / then
        assertThrows(RuntimeException.class, () -> todoService.findTodo(1L));
    }

    @Test
    public void updateTodoTest() throws Exception {
        // given
        Todo patchTodo = new Todo(1L, null, 1L, false);
        Todo findTodo = new Todo(1L, "양치하기", 1L, false);

        given(todoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(findTodo));
        given(todoRepository.save(Mockito.any(Todo.class))).willReturn(findTodo);

        // when
        Todo updateTodo = todoService.updateTodo(patchTodo);

        // then
        assertEquals(patchTodo.getTodoId(), updateTodo.getTodoId());
        assertNotEquals(patchTodo.getTitle(), updateTodo.getTitle());
        assertEquals(patchTodo.getOrder(), updateTodo.getOrder());
        assertEquals(patchTodo.isCompleted(), updateTodo.isCompleted());
    }

    @Test
    public void deleteTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);

        given(todoRepository.findById(Mockito.anyLong())).willReturn(Optional.of(todo));
        doNothing().when(todoRepository).delete(todo);

        // when / then
        todoService.deleteTodo(todo.getTodoId());
    }

}