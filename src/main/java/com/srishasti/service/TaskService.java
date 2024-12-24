package com.srishasti.service;

import com.srishasti.context.UserContext;
import com.srishasti.model.Task;
import com.srishasti.repo.TaskRepo;
import com.srishasti.repo.UserRepo;
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

    public Task addTask(Task task) {

        if(task.getDeadline() != null){
            if(task.getDeadline().isBefore(LocalDate.now())){
                task.setStatus("overdue");
                String difficulty = task.getDifficulty();
                int userId = UserContext.getUserId();
                if(difficulty.equals("easy"))
                    profileService.changeHealth(userId,3);
                else if (difficulty.equals("medium"))
                    profileService.changeHealth(userId, 5);
                else profileService.changeHealth(userId, 7);
            }
        }

        task.setUserId(UserContext.getUserId());
        taskRepo.save(task);
        return task;
    }

    public String completeTask(int taskId) {

        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null) return "not_found";
        if(task.getUserId() != UserContext.getUserId()) return "bad_request";
        if(task.getStatus().equals("completed")) return "conflict";

        int penaltyFactor = 1;
        if(task.getStatus().equals("overdue")) penaltyFactor = 2;

        task.setStatus("completed");
        taskRepo.save(task);

        if(task.getDifficulty().equals("easy"))
            profileService.changeXp(10/penaltyFactor);
        else if (task.getDifficulty().equals("medium"))
            profileService.changeXp(15/penaltyFactor);
        else profileService.changeXp(20/penaltyFactor);

        return "ok";

    }

    public String deleteTask(int taskId) {
        Task task = taskRepo.findById(taskId).orElse(null);
        if(task == null) return "not_found";
        if(task.getUserId() != UserContext.getUserId()) return "bad_request";

        taskRepo.deleteById(taskId);
        return "ok";

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
