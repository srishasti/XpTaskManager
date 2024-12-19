package com.srishasti.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int taskId;
    private int userId = 0;

    @NotBlank
    private String taskname;
    private String taskDesc = "";

    private String difficulty = "easy";
    private String status = "pending";
    private LocalDate deadline = null;

}

