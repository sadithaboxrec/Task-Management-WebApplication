package com.sadi.tasks.service.impl;

import com.sadi.tasks.dto.Response;
import com.sadi.tasks.dto.TaskRequest;
import com.sadi.tasks.entity.Project;
import com.sadi.tasks.entity.Task;
import com.sadi.tasks.entity.User;
import com.sadi.tasks.enums.Priority;
import com.sadi.tasks.exceptions.BadRequestException;
import com.sadi.tasks.exceptions.NotFoundException;
import com.sadi.tasks.repo.ProjectRepo;
import com.sadi.tasks.repo.TaskRepo;
import com.sadi.tasks.service.TaskService;
import com.sadi.tasks.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepo taskRepo;
    private final ProjectRepo projectRepo;
    private final UserService userService;

    @Override
    public Response<Task> createTask(TaskRequest taskRequest, Long projectId) {
        log.info("Creating Task under Project ID: {}", projectId);

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project Not Found"));

        // check date collision
        if (taskRequest.getDueDate() != null &&
                project.getStartDate() != null &&
                taskRequest.getDueDate().isBefore(project.getStartDate())) {

            throw new BadRequestException(
                    "Task cannot start before project start date"
            );
        }

        User currentUser = userService.getCurrentLoggedUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to add tasks to this project");
        }

        Task task = Task.builder()
                .title(taskRequest.getTitle())
                .description(taskRequest.getDescription())
                .completed(taskRequest.isCompleted())
                .priority(taskRequest.getPriority())
                .dueDate(taskRequest.getDueDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .project(project)
                .build();

        Task savedTask = taskRepo.save(task);

        return Response.<Task>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Task Created Successfully")
                .data(savedTask)
                .build();
    }

    @Override
    public Response<Task> updateTask(TaskRequest taskRequest) {
        log.info("Updating Task ID: {}", taskRequest.getId());

        Task task = taskRepo.findById(taskRequest.getId())
                .orElseThrow(() -> new NotFoundException("Task Not Found"));


        User currentUser = userService.getCurrentLoggedUser();
        if (!task.getProject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to update this task");
        }

        if (taskRequest.getTitle() != null) {
            task.setTitle(taskRequest.getTitle());
        }

        if (taskRequest.getDescription() != null) {
            task.setDescription(taskRequest.getDescription());
        }

        task.setCompleted(taskRequest.isCompleted());

        if (taskRequest.getPriority() != null) {
            task.setPriority(taskRequest.getPriority());
        }

        if (taskRequest.getDueDate() != null) {

            Project project = task.getProject();

            if (project.getStartDate() != null &&
                    taskRequest.getDueDate().isBefore(project.getStartDate())) {

                throw new BadRequestException(
                        "Task due date cannot be before project start date"
                );
            }

            task.setDueDate(taskRequest.getDueDate());
        }


        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask = taskRepo.save(task);

        return Response.<Task>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task Updated Successfully")
                .data(updatedTask)
                .build();
    }

    @Override
    public Response<Void> deleteTask(Long taskId) {
        log.info("Deleting Task ID: {}", taskId);

        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task Not Found"));


        User currentUser = userService.getCurrentLoggedUser();
        if (!task.getProject().getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to delete this task");
        }

        taskRepo.delete(task);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Task Deleted Successfully")
                .build();
    }

    @Override
    public Response<List<Task>> getTasksByProject(Long projectId) {
        log.info("Fetching tasks for Project ID: {}", projectId);

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project Not Found"));


        User currentUser = userService.getCurrentLoggedUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to view tasks for this project");
        }

        List<Task> tasks = taskRepo.findByProject(project, Sort.by(Sort.Direction.DESC, "id"));

        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tasks fetched successfully")
                .data(tasks)
                .build();
    }

    @Override
    @Transactional
    public Response<List<Task>> getTasksByCompletionStatus(Long projectId, boolean completed) {
        log.info("Fetching tasks for Project ID: {} by completed={}", projectId, completed);

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project Not Found"));

        User currentUser = userService.getCurrentLoggedUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to view tasks for this project");
        }

        List<Task> tasks = taskRepo.findByCompletedAndProject(completed, project);

        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tasks filtered by completion status")
                .data(tasks)
                .build();
    }

    @Override
    public Response<List<Task>> getTasksByPriority(Long projectId, String priority) {
        log.info("Fetching tasks for Project ID: {} by priority={}", projectId, priority);

        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project Not Found"));

        User currentUser = userService.getCurrentLoggedUser();
        if (!project.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You do not have permission to view tasks for this project");
        }

        Priority priorityEnum = Priority.valueOf(priority.toUpperCase());
        List<Task> tasks = taskRepo.findByPriorityAndProject(priorityEnum, project, Sort.by(Sort.Direction.DESC, "id"));

        return Response.<List<Task>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Tasks filtered by priority")
                .data(tasks)
                .build();
    }
}
