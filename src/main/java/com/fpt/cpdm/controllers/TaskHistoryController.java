package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.tasks.histories.TaskHistoryResponse;
import com.fpt.cpdm.services.TaskHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks/histories")
public class TaskHistoryController {

    private final TaskHistoryService taskHistoryService;

    @Autowired
    public TaskHistoryController(TaskHistoryService taskHistoryService) {
        this.taskHistoryService = taskHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskHistoryResponse> readAllHistories(@PathVariable("id") Integer id) {

        TaskHistoryResponse taskHistoryResponse = taskHistoryService.findById(id);

        return ResponseEntity.ok(taskHistoryResponse);
    }
}
