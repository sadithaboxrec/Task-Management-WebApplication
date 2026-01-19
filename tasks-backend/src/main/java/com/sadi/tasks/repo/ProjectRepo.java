package com.sadi.tasks.repo;

import com.sadi.tasks.entity.Project;
import com.sadi.tasks.entity.User;
import com.sadi.tasks.enums.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {

    List<Project> findByName(String name, Sort sort);

    List<Project> findByUser(User user, Sort sort);

    List<Project> findByCompletedAndUser(boolean completed, User user);

    List<Project> findByPriorityAndUser(Priority priority, User user,Sort sort);
}
