package com.sadi.tasks.controller;

import com.sadi.tasks.dto.ProjectRequest;
import com.sadi.tasks.dto.Response;
import com.sadi.tasks.entity.Project;
import com.sadi.tasks.service.ProjectService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<Response<Project>> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        return ResponseEntity.ok(projectService.createProject(projectRequest));
    }

    @PutMapping
    public ResponseEntity<Response<Project>> updateProject( @RequestBody ProjectRequest projectRequest) {
        return ResponseEntity.ok(projectService.updateProject(projectRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<Project>>> getAllProjects()  {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Project>> getProjectById( @PathVariable Long id)  {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Response<Void>> deleteProject( @PathVariable Long id)  {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }

    @GetMapping("/status")
    public ResponseEntity<Response<List<Project>>> getTasksByStatus(@RequestParam boolean completed)  {
        return ResponseEntity.ok(projectService.getProjectsByCompleteStatus(completed));
    }

    @GetMapping("/priority")
    public ResponseEntity<Response<List<Project>>> getTasksByPriority(@RequestParam String priority)  {
        return ResponseEntity.ok(projectService.getProjectsByPriority(priority)  );
    }


}
