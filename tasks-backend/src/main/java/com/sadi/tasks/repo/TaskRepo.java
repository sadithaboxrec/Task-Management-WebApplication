package com.sadi.tasks.repo;

import com.sadi.tasks.entity.Task;
import com.sadi.tasks.entity.User;
import com.sadi.tasks.enums.Priority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {

    List<Task> findByName(String User, Sort sort);

    List<Task> findByCompletedAndUser(boolean completed, User user);

    List<Task> findByPriorityAndUser(Priority priority, User user, Sort sort);

}
