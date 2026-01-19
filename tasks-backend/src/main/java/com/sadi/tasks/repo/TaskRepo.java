package com.sadi.tasks.repo;

import com.sadi.tasks.entity.Project;
import com.sadi.tasks.entity.Task;
import com.sadi.tasks.entity.User;
import com.sadi.tasks.enums.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

//    List<Task> findByTitle(String title, Sort sort);
//
//    List<Task> findByCompletedAndProject_User(boolean completed, User user);
//
//    List<Task> findByPriorityAndProject_User(Priority priority, User user, Sort sort);


    // Fetch all tasks under a project, sorted
    List<Task> findByProject(Project project, Sort sort);

    // Fetch tasks by completion status under a project
    List<Task> findByCompletedAndProject(boolean completed, Project project);

    // Fetch tasks by priority under a project, sorted
    List<Task> findByPriorityAndProject(Priority priority, Project project, Sort sort);

    // Optional: fetch by title under a project (not global)
    List<Task> findByTitleAndProject(String title, Project project, Sort sort);

}
