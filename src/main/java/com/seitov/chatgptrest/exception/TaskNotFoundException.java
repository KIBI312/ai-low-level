package com.seitov.chatgptrest.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task not found with ID: " + taskId);
    }
}

