package com.sadi.tasks.service;

import com.sadi.tasks.dto.Response;
import com.sadi.tasks.dto.TaskRequest;
import com.sadi.tasks.entity.Task;


import java.util.List;

public interface TaskService {

//    Response<Task> createProject(Task task);
//    Response<List<Task>> getAllProjects();
//    Response<Task> getProjectById(Long id);
//    Response<Task> updateProject(Long id, Task task);
//    Response<Void> deleteProject(Long id);
//    Response<List<Task>> getProjectsByCompleteStatus(boolean completeStatus);
//    Response<List<Task>> getProjectsByPriority(String priority);

    Response<Task> createTask(TaskRequest taskRequest, Long projectId);

    Response<Task> updateTask(TaskRequest taskRequest);

    Response<Void> deleteTask(Long taskId);

    Response<List<Task>> getTasksByProject(Long projectId);

    Response<List<Task>> getTasksByCompletionStatus(Long projectId, boolean completed);

    Response<List<Task>> getTasksByPriority(Long projectId, String priority);


}
