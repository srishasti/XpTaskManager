package com.srishasti.model;

import lombok.Data;

@Data
public class TaskUpdate {
    private Task task;
    private String status;
    private int xp;
    private int health;

    public TaskUpdate(Task task, String status, int xp, int health) {
        this.task = task;
        this.status = status;
        this.xp = xp;
        this.health = health;
    }

    public TaskUpdate(String status) {
        this.status = status;
        this.task = null;
        this.xp = 0;
        this.health = 0;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
