package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.Task;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> readAll() {

        List<Task> tasks = taskService.findAll();
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<Task> create(@Valid @RequestBody Task task,
                                       BindingResult result) {

        return save(null, task, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> update(@PathVariable(name = "id") Integer id,
                                       @Valid @RequestBody Task task,
                                       BindingResult result) {

        return save(id, task, result);
    }

    private ResponseEntity<Task> save(Integer id, Task task, BindingResult result) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        task.setId(id);
        Task savedDocument = taskService.save(task);

        return ResponseEntity.ok(savedDocument);
    }
}
