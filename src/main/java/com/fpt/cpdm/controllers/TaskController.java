package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List> readAll(
            @RequestParam(name = "summary", defaultValue = "false") boolean isSummary) {

        List tasks;
        if (isSummary) {
            tasks = taskService.findAllSummary();
        } else {
            tasks = taskService.findAll();
        }
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> readById(@PathVariable(name = "id") Integer id) {

        Task task = taskService.findById(id);

        return ResponseEntity.ok(task);
    }

    @GetMapping("/findByExecutor")
    public ResponseEntity<List<TaskSummary>> findByExecutor(@RequestParam("id") Integer id) {

        // create id only user for finding
        User user = new User();
        user.setId(id);

        List<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutor(user);
        if (taskSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/findByCurrentLoggedExecutor")
    public ResponseEntity<List<TaskSummary>> findByCurrentLoggedExecutor(Principal principal) {

        // get current logged executor
        User user = userService.findByEmail(principal.getName());

        List<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutor(user);
        if (taskSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/findByCurrentLoggedCreator")
    public ResponseEntity<List<TaskSummary>> findByCurrentLoggedCreator(Principal principal) {

        // get current logged creator
        User user = userService.findByEmail(principal.getName());

        List<TaskSummary> taskSummaries = taskService.findAllSummaryByCreator(user);
        if (taskSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
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


    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {

        if (taskService.existsById(id) == false) {
            throw new TaskNotFoundException(id);
        }
        taskService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
