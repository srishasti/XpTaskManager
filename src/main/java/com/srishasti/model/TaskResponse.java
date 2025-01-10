package com.srishasti.model;

import lombok.Data;

@Data
public class TaskResponse {
    private Task task;
    private int xp;
    private int health;

    public TaskResponse(TaskUpdate update){
        this.task = update.getTask();
        this.xp = update.getXp();
        this.health = update.getHealth();
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
