package toyproject.todo.todo.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import toyproject.todo.todo.dto.TodoPostDto;
import toyproject.todo.todo.entity.Todo;
import toyproject.todo.todo.mapper.TodoMapper;
import toyproject.todo.todo.service.TodoService;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TodoService todoService;

    @Autowired
    private TodoMapper mapper;

    @Test
    public void postTodoTest() throws Exception {
        // given
        TodoPostDto post = new TodoPostDto("양치하기", 1L, false);
        Todo todo = mapper.todoPostToTodo(post);
        System.out.println(todo.getTodoId());

        // when
        given(todoService.saveTodo(Mockito.any(Todo.class))).willReturn(todo);
        String content = gson.toJson(post);
        ResultActions actions =
                mockMvc.perform(
                        post("/todos")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        MvcResult result = actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.order").value(todo.getOrder()))
                .andExpect(jsonPath("$.completed").value(todo.isCompleted()))
                .andReturn();
    }

    @Test
    public void getTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);

        // when
        given(todoService.findTodo(Mockito.anyLong())).willReturn(todo);

        URI getUri = UriComponentsBuilder.newInstance().path("/todos/{todo-id}")
                .buildAndExpand(todo.getTodoId()).toUri();

        ResultActions actions = mockMvc.perform(
                get(getUri)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todoId").value(todo.getTodoId()))
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.order").value(todo.getOrder()))
                .andExpect(jsonPath("$.completed").value(todo.isCompleted()))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void getTodosTest() throws Exception {
        // given
        Todo todo1 = new Todo(1L, "양치하기", 1L, false);
        Todo todo2 = new Todo(2L, "세수하기", 2L, false);

        int page = 1;
        int size = 10;

        Page<Todo> pageTodos = new PageImpl<>(List.of(todo1, todo2),
                PageRequest.of(page - 1, size, Sort.by("todoId").descending()), 2);


        // when
        given(todoService.findTodos(Mockito.anyInt(), Mockito.anyInt())).willReturn(pageTodos);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));

        ResultActions actions = mockMvc.perform(
                get("/todos")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        MvcResult result = actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].todoId").value(todo1.getTodoId()))
                .andExpect(jsonPath("$.[0].title").value(todo1.getTitle()))
                .andExpect(jsonPath("$.[0].order").value(todo1.getOrder()))
                .andExpect(jsonPath("$.[0].completed").value(todo1.isCompleted()))
                .andExpect(jsonPath("$.[1].todoId").value(todo2.getTodoId()))
                .andExpect(jsonPath("$.[1].title").value(todo2.getTitle()))
                .andExpect(jsonPath("$.[1].order").value(todo2.getOrder()))
                .andExpect(jsonPath("$.[1].completed").value(todo2.isCompleted()))
                .andReturn();
    }

    @Test
    public void deleteTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);
        doNothing().when(todoService).deleteTodo(todo.getTodoId());

        // when
        ResultActions actions = mockMvc.perform(
                delete("/todos/{todo-id}", 1L)
        );

        // then
        actions.andExpect(status().isNoContent());
    }

}