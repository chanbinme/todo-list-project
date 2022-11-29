package toyproject.todo.todo.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import toyproject.todo.todo.dto.TodoPatchDto;
import toyproject.todo.todo.dto.TodoPostDto;
import toyproject.todo.todo.dto.TodoResponseDto;
import toyproject.todo.todo.entity.Todo;
import toyproject.todo.todo.mapper.TodoMapper;
import toyproject.todo.todo.service.TodoService;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TodoController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private TodoService todoService;

    @MockBean
    private TodoMapper mapper;

    @Test
    public void postTodoTest() throws Exception {
        // given
        TodoPostDto post = new TodoPostDto("양치하기", 1L, false);
        String content = gson.toJson(post);
        Todo todo = new Todo(1L, "양치하기", 1L, false);
        TodoResponseDto responseDto = new TodoResponseDto(1L, "양치하기", 1L, false);

        // when
        given(mapper.todoPostToTodo(Mockito.any(TodoPostDto.class))).willReturn(todo);
        given(todoService.saveTodo(Mockito.any(Todo.class))).willReturn(todo);
        given(mapper.todoToTodoResponse(Mockito.any(Todo.class))).willReturn(responseDto);

        ResultActions actions =
                mockMvc.perform(
                        post("/todos")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.order").value(post.getOrder()))
                .andExpect(jsonPath("$.completed").value(post.isCompleted()))
                .andDo(document("post-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("todoId").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
    }

    @Test
    public void getTodoTest() throws Exception {
        // given
        Todo todo = new Todo(1L, "양치하기", 1L, false);
        TodoResponseDto responseDto = new TodoResponseDto(1L, "양치하기", 1L, false);

        // when
        given(todoService.findTodo(Mockito.anyLong())).willReturn(todo);
        given(mapper.todoToTodoResponse(Mockito.any(Todo.class))).willReturn(responseDto);

        ResultActions actions = mockMvc.perform(
                get("/todos/{todo-id}", todo.getTodoId())
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todoId").value(todo.getTodoId()))
                .andExpect(jsonPath("$.title").value(todo.getTitle()))
                .andExpect(jsonPath("$.order").value(todo.getOrder()))
                .andExpect(jsonPath("$.completed").value(todo.isCompleted()))
                .andDo(document("get-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                Arrays.asList(
                                        parameterWithName("todo-id").description("Todo 식별자"))
                        ),
                        responseFields(
                                Arrays.asList(
                                        fieldWithPath("todoId").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
    }

    @Test
    public void getTodosTest() throws Exception {
        // given
        Todo todo1 = new Todo(1L, "양치하기", 1L, false);
        Todo todo2 = new Todo(2L, "세수하기", 2L, false);
        TodoResponseDto responseDto1 = new TodoResponseDto(1L, "양치하기", 1L, false);
        TodoResponseDto responseDto2 = new TodoResponseDto(2L, "세수하기", 2L, false);

        int page = 1;
        int size = 10;

        Page<Todo> pageTodos = new PageImpl<>(List.of(todo1, todo2),
                PageRequest.of(page - 1, size), 2);
        List<TodoResponseDto> responseDtos = List.of(responseDto1, responseDto2);

        // when
        given(todoService.findTodos(Mockito.anyInt(), Mockito.anyInt())).willReturn(pageTodos);
        given(mapper.todosToTodoResponses(Mockito.anyList())).willReturn(responseDtos);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", String.valueOf(page));
        params.add("size", String.valueOf(size));

        ResultActions actions = mockMvc.perform(
                get("/todos")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON)
        );
        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].todoId").value(todo1.getTodoId()))
                .andExpect(jsonPath("$.[0].title").value(todo1.getTitle()))
                .andExpect(jsonPath("$.[0].order").value(todo1.getOrder()))
                .andExpect(jsonPath("$.[0].completed").value(todo1.isCompleted()))
                .andExpect(jsonPath("$.[1].todoId").value(todo2.getTodoId()))
                .andExpect(jsonPath("$.[1].title").value(todo2.getTitle()))
                .andExpect(jsonPath("$.[1].order").value(todo2.getOrder()))
                .andExpect(jsonPath("$.[1].completed").value(todo2.isCompleted()))
                .andDo(document("get-todos",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("page").description("Page 번호"),
                                        parameterWithName("size").description("Page Size")
                                )),
                        responseFields(
                                List.of(
                                        fieldWithPath("[].todoId").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("[].order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("[].completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
    }

    @Test
    public void patchTodoTest() throws Exception {
        // given
        TodoPatchDto patchDto = TodoPatchDto.builder().title("양치하기").build();
        Todo todo = new Todo(1L, "양치하기", 1L, false);
        TodoResponseDto responseDto = new TodoResponseDto(1L, "양치하기", 1L, false);
        String content = gson.toJson(patchDto);

        // when
        given(mapper.todoPatchToTodo(Mockito.any(TodoPatchDto.class))).willReturn(todo);
        given(todoService.updateTodo(Mockito.any(Todo.class))).willReturn(todo);
        given(mapper.todoToTodoResponse(Mockito.any(Todo.class))).willReturn(responseDto);


        ResultActions actions = mockMvc.perform(
                patch("/todos/{todo-id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.todoId").value(1L))
                .andExpect(jsonPath("$.title").isNotEmpty())
                .andExpect(jsonPath("$.order").isNotEmpty())
                .andExpect(jsonPath("$.completed").isNotEmpty())
                .andDo(document("patch-todo",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                Arrays.asList(
                                        parameterWithName("todo-id").description("Todo 식별자"))
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일").optional(),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위").optional(),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부").optional())
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("todoId").type(JsonFieldType.NUMBER).description("Todo 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("해야 할 일"),
                                        fieldWithPath("order").type(JsonFieldType.NUMBER).description("우선 순위"),
                                        fieldWithPath("completed").type(JsonFieldType.BOOLEAN).description("완료 여부")
                                )
                        )
                ));
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
        actions.andExpect(status().isNoContent())
                .andDo(document("delete-todo",
                        pathParameters(List.of(
                                parameterWithName("todo-id").description("Todo 식별자")
                        ))));
    }
}