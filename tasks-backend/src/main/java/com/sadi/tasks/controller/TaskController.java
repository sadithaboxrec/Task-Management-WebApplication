package com.sadi.tasks.controller;

import com.sadi.tasks.dto.Response;
import com.sadi.tasks.dto.TaskRequest;
import com.sadi.tasks.entity.Task;
import com.sadi.tasks.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Create Task under a Project
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Response<Task>> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequest taskRequest) {

        return ResponseEntity.ok(taskService.createTask(taskRequest, projectId));
    }

    // Get all tasks under a project
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Response<List<Task>>> getTasksByProject(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    // Update task
    @PutMapping("/tasks")
    public ResponseEntity<Response<Task>> updateTask(
            @Valid @RequestBody TaskRequest taskRequest) {

        return ResponseEntity.ok(taskService.updateTask(taskRequest));
    }

    // Delete task
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Response<Void>> deleteTask(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(taskService.deleteTask(taskId));
    }

    // Filter by status
    @GetMapping("/projects/{projectId}/tasks/status")
    public ResponseEntity<Response<List<Task>>> getTasksByStatus(
            @PathVariable Long projectId,
            @RequestParam boolean completed) {

        return ResponseEntity.ok(
                taskService.getTasksByCompletionStatus(projectId, completed)
        );
    }

    // Filter by priority
    @GetMapping("/projects/{projectId}/tasks/priority")
    public ResponseEntity<Response<List<Task>>> getTasksByPriority(
            @PathVariable Long projectId,
            @RequestParam String priority) {

        return ResponseEntity.ok(
                taskService.getTasksByPriority(projectId, priority)
        );
    }
}
