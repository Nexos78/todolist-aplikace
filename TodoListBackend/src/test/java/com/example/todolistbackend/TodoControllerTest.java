package com.example.todolistbackend;

import com.example.todolistbackend.todo.TodoEntry;
import com.example.todolistbackend.todo.TodoController;
import com.example.todolistbackend.todo.TodoRepository;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoRepository todoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldReturnAllTodos() throws Exception {
        TodoEntry test1 = new TodoEntry(1L, "ahoj", false);
        TodoEntry test2 = new TodoEntry(2L, "nazdar", true);
        TodoEntry test3 = new TodoEntry(3L, "sbohem", false);

        when(todoRepository.findAll()).thenReturn(List.of(test1, test2, test3));

        mockMvc.perform(get("/api/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))

                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("ahoj"))
                .andExpect(jsonPath("$[0].completed").value(false))

                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("nazdar"))
                .andExpect(jsonPath("$[1].completed").value(true))

                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].title").value("sbohem"))
                .andExpect(jsonPath("$[2].completed").value(false));
    }

    @Test
    public void shouldCreateNewTodo() throws Exception {
        TodoEntry newTodo = new TodoEntry(null, "naučit se testovat", false);

        TodoEntry savedTodo = new TodoEntry(1L, "naučit se testovat", false);

        when(todoRepository.save(any(TodoEntry.class))).thenReturn(savedTodo);

        mockMvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newTodo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("naučit se testovat"))
                .andExpect(jsonPath("$.completed").value(false));

                verify(todoRepository).save(any(TodoEntry.class));
    }
}
