package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.Task;
import com.fpt.cpdm.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController  {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> readAll(){

        List<Task> tasks = taskService.findAll();
        if(tasks.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }
}
