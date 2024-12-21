package com.srishasti.controller;

import com.srishasti.model.Task;
import com.srishasti.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    Map<String, HttpStatus> statusMap = new HashMap<>(Map.of(
            "no_content" , HttpStatus.OK,
            "conflict", HttpStatus.CONFLICT,
            "bad_request", HttpStatus.BAD_REQUEST,
            "ok", HttpStatus.OK
    ));

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(){
        List<Task> tasks = taskService.getTasks();
        if(tasks.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable int taskId){
        Task task = taskService.getTask(taskId);
        if(task == null) return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PostMapping("/tasks")
    public ResponseEntity<List<Task>> addTask(@Valid @RequestBody Task task){
        taskService.addTask(task);
        return new ResponseEntity<>(taskService.getTasks(),HttpStatus.OK);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<List<Task>> completeTask(@PathVariable int taskId){
        String string = taskService.completeTask(taskId);
        return new ResponseEntity<>(taskService.getTasks(),statusMap.getOrDefault(string,HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<List<Task>> deleteTask(@PathVariable int taskId){
        String string = taskService.deleteTask(taskId);
        return new ResponseEntity<>(taskService.getTasks(),statusMap.getOrDefault(string,HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
