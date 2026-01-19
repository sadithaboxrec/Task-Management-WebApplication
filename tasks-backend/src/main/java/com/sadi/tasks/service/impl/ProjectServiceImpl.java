package com.sadi.tasks.service.impl;

import com.sadi.tasks.dto.ProjectRequest;
import com.sadi.tasks.dto.Response;
import com.sadi.tasks.entity.Project;
import com.sadi.tasks.entity.User;
import com.sadi.tasks.enums.Priority;
import com.sadi.tasks.exceptions.NotFoundException;
import com.sadi.tasks.repo.ProjectRepo;
import com.sadi.tasks.repo.TaskRepo;
import com.sadi.tasks.service.ProjectService;
import com.sadi.tasks.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectServiceImpl implements ProjectService {



    private final ProjectRepo projectRepo;
    private final UserService userService;
    private final TaskRepo taskRepo;


    @Override
    public Response<Project> createProject(ProjectRequest projectRequest) {

        log.info("Project Create");

        User currentuser =userService.getCurrentLoggedUser();

        Project projectsave = Project.builder()
                .name(projectRequest.getName())
                .description(projectRequest.getDescription())
                .completed(projectRequest.getCompleted())
                .priority(projectRequest.getPriority())
                .dueDate(projectRequest.getDueDate())
                .startDate(projectRequest.getStartDate())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(currentuser)
                .build();

        Project savedProject=projectRepo.save(projectsave);

        return Response.<Project>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Project Created Successfully")
                .data(savedProject)
                .build();
    }


//    public Response<List<Project>> getAllProjects() {
//
//        log.info("Get All Projects");
//
//        User currentuser =userService.getCurrentLoggedUser();
//
//        List<Project> projects=projectRepo.findByCompletedAndUser(currentuser, Sort.by(Sort.Direction.DESC, "id"));
//
//        return Response.<List<Project>>builder()
//                .statusCode(HttpStatus.OK.value())
//                .message("All Projects Here")
//                .data(projects)
//                .build();
//
//    }


    @Override
    public Response<List<Project>> getAllProjects() {

        log.info("Get All Projects");

        User currentUser = userService.getCurrentLoggedUser();

        List<Project> projects =
                projectRepo.findByUser(
                        currentUser,
                        Sort.by(Sort.Direction.DESC, "id")
                );

        return Response.<List<Project>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("All Projects Here")
                .data(projects)
                .build();
    }


    @Override
    public Response<Project> getProjectById(Long id) {

        log.info("Get Project by ID");

        Project project = projectRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Project Not Found"));

        return Response.<Project>builder()
                .statusCode(HttpStatus.OK.value())
                .message(" Project Here")
                .data(project)
                .build();
    }

    @Override
    public Response<Project> updateProject(ProjectRequest projectRequest) {

        log.info("Update Project by ID");

        Project project= projectRepo.findById(projectRequest.getId())
                .orElseThrow(() -> new NotFoundException("Project Not Found"));

        if(projectRequest.getName()!=null) {
            project.setName(projectRequest.getName());
        }
        if(projectRequest.getDescription()!=null) {
            project.setDescription(projectRequest.getDescription());
        }
        if (projectRequest.getDueDate()!=null) {
            project.setDueDate(projectRequest.getDueDate());
        }
        if(projectRequest.getStartDate()!=null) {
            project.setStartDate(projectRequest.getStartDate());
        }
        if(projectRequest.getPriority()!=null) {
            project.setPriority(projectRequest.getPriority());
        }

        if (projectRequest.getCompleted() != null) {
            project.setCompleted(projectRequest.getCompleted());
        }

        project.setUpdatedAt(LocalDateTime.now());

        Project updatedProject=projectRepo.save(project);

        return Response.<Project>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Project Updated Successfully")
                .data(project)
                .build();

    }

    @Override
    public Response<Void> deleteProject(Long id) {
        log.info("Delete Project by ID");

        if(!projectRepo.existsById(id)) {
            throw new NotFoundException("Project Not Found");
        }
        projectRepo.deleteById(id);

        return Response.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Project Deleted Succesfflly")
                .build();
    }

    @Override
    @Transactional
    public Response<List<Project>> getProjectsByCompleteStatus(boolean completeStatus) {


        log.info("inside getProjectsByCompleteStatus()");

        User currentUser = userService.getCurrentLoggedUser();

        List<Project> projects = projectRepo.findByCompletedAndUser(completeStatus, currentUser);

        return Response.<List<Project>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Project filtered by completion status for user")
                .data(projects)
                .build();

    }

    @Override
    public Response<List<Project>> getProjectsByPriority(String priority) {


        log.info("inside getProjectsByPriority()");

        User currentUser = userService.getCurrentLoggedUser();

        Priority priorityEnum = Priority.valueOf(priority.toUpperCase());

        List<Project> projects = projectRepo.
                findByPriorityAndUser(priorityEnum, currentUser, Sort.by(Sort.Direction.DESC, "id"));

        return Response.<List<Project>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Projects filtered by priority for user")
                .data(projects)
                .build();
    }
}
