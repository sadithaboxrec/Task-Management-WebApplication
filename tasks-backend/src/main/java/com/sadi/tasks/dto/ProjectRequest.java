package com.sadi.tasks.dto;

import com.sadi.tasks.enums.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectRequest {

    private Long Id;

    @NotBlank(message= "Project Name is required")
    @Size(max = 200,message = "Project Name must be less than 200 characters")
    private String name;

    @Size(max = 500,message = "Description must be less than 500 characters")
    private String description;

 //   @NotNull(message = "Completed status is required")
    private Boolean completed;

    @NotNull(message = "Priority is required")
    private Priority priority;

    @FutureOrPresent(message = "Due date must be future or present from today")
    private LocalDateTime startDate;
    @FutureOrPresent(message = "Due date must be future or present from today")
    private LocalDateTime dueDate;
}
