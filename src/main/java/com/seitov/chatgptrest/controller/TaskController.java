package com.seitov.chatgptrest.controller;

import com.seitov.chatgptrest.entity.Task;
import com.seitov.chatgptrest.entity.User;
import com.seitov.chatgptrest.service.CustomUserDetailsService;
import com.seitov.chatgptrest.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final CustomUserDetailsService userService;

    @Autowired
    public TaskController(TaskService taskService, CustomUserDetailsService userService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        // Get the currently authenticated user's details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Assuming the user's username is their identification field (you may customize this)
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());

        // Associate the task with the currently authenticated user
        task.setUser(user);

        Task createdTask = taskService.createTask(task);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task updatedTask) {
        Task updated = taskService.updateTask(taskId, updatedTask);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Task>> getTasksByUser(Authentication authentication) {
        // Get the currently authenticated user's details
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Assuming the user's username is their identification field (you may customize this)
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());

        // Retrieve tasks associated with the authenticated user by their username
        List<Task> tasks = taskService.getTasksByUserId(user.getId());

        return ResponseEntity.ok(tasks);
    }

}
