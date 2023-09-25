package com.seitov.chatgptrest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seitov.chatgptrest.entity.Task;
import com.seitov.chatgptrest.entity.User;
import com.seitov.chatgptrest.service.CustomUserDetailsService;
import com.seitov.chatgptrest.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    private final
    ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private TaskService taskService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testCreateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Test Task");

        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(new User(1L,"testuser", "password"));
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Test Task"));

        verify(customUserDetailsService, times(1)).loadUserByUsername("testuser");
        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testUpdateTask() throws Exception {
        Long taskId = 1L;
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");

        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(eq(taskId), any(Task.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testDeleteTask() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(taskService, times(1)).deleteTask(eq(taskId));
    }

    @Test
    @WithMockUser(username = "testuser")
    public void testGetTasksByUser() throws Exception {
        List<Task> tasks = new ArrayList<>();
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        tasks.add(task1);
        tasks.add(task2);

        when(customUserDetailsService.loadUserByUsername(anyString())).thenReturn(new User(1L, "testuser", "password"));
        when(taskService.getTasksByUserId(anyLong())).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Task 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Task 2"));

        verify(customUserDetailsService, times(1)).loadUserByUsername("testuser");
        verify(taskService, times(1)).getTasksByUserId(anyLong());
    }

}
