package com.sadi.tasks.service;

import com.sadi.tasks.dto.ProjectRequest;
import com.sadi.tasks.dto.Response;
import com.sadi.tasks.entity.Project;

import java.util.List;

public interface ProjectService {

    Response<Project> createProject(ProjectRequest projectRequest);
    Response<List<Project>> getAllProjects();
    Response<Project> getProjectById(Long id);
    Response<Project> updateProject(ProjectRequest projectRequest);
    Response<Void> deleteProject(Long id);
    Response<List<Project>> getProjectsByCompleteStatus(boolean completeStatus);
    Response<List<Project>> getProjectsByPriority(String priority);
}
