package com.srishasti.service;

import com.srishasti.context.UserContext;
import com.srishasti.model.Task;
import com.srishasti.model.TaskUpdate;
import com.srishasti.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepo taskRepo;

    @Autowired
    private ProfileService profileService;
    
    public List<Task> getTasks() {
        int userId = UserContext.getUserId();
        return taskRepo.findByUserId(userId);
    }

    public TaskUpdate addTask(Task task) {

        int healthChanged = 0;
        int xpChanged = 0;

        if(task.getDifficulty().isEmpty()) task.setDifficulty("easy");

        if(task.getDeadline() != null){
            if(task.getDeadline().isBefore(LocalDate.now())){
                task.setStatus("overdue");
                String difficulty = task.getDifficulty();
                int userId = UserContext.getUserId();
                if(difficulty.equals("easy"))
                    healthChanged = 3;
                else if (difficulty.equals("medium"))
                    healthChanged = 5;
                else healthChanged = 7;
                profileService.changeHealth(userId, healthChanged);
            }
        }

        task.setUserId(UserContext.getUserId());
        taskRepo.save(task);

        TaskUpdate update = new TaskUpdate(task,"ok",xpChanged,healthChanged);
        return update;
    }

    public TaskUpdate completeTask(int taskId) {

        int healthChanged = 0;
        int xpChanged = 0;

        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null) return new TaskUpdate("not_found");
        if(task.getUserId() != UserContext.getUserId()) return new TaskUpdate("bad_request");
        if(task.getStatus().equals("completed")) return new TaskUpdate("conflict");

        int penaltyFactor = 1;
        if(task.getStatus().equals("overdue")) penaltyFactor = 2;

        task.setStatus("completed");
        taskRepo.save(task);

        if(task.getDifficulty().equals("easy"))
            xpChanged = 10/penaltyFactor;
        else if (task.getDifficulty().equals("medium"))
            xpChanged = 15/penaltyFactor;
        else xpChanged = 20/penaltyFactor;

        profileService.changeXp(xpChanged/penaltyFactor);

        return new TaskUpdate(task,"ok",xpChanged,healthChanged);

    }

    public TaskUpdate deleteTask(int taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null) return new TaskUpdate("not_found");
        if(task.getUserId() != UserContext.getUserId()) return new TaskUpdate("bad_request");

        taskRepo.deleteById(taskId);
        return new TaskUpdate(task,"ok",0,0);

    }

    public Task getTask(int taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        return task;
    }

    public void checkOverdueTasks(){

        List<Task> tasks = taskRepo.getOverdueTasks();
        for(Task task : tasks){

            int userId = task.getUserId();
            String difficulty = task.getDifficulty();
            if(difficulty.equals("easy"))
                profileService.changeHealth(userId,5);
            else if (difficulty.equals("medium"))
                profileService.changeHealth(userId, 10);
            else profileService.changeHealth(userId, 15);

            task.setStatus("overdue");
            taskRepo.save(task);
        }

    }
}
