package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
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
    public ResponseEntity<List<TaskSummary>> readAll() {

        List<TaskSummary> tasks = taskService.findAllSummary();
        if (tasks.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskSummary> readById(@PathVariable(name = "id") Integer id) {
        return null;
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
    public ResponseEntity<TaskSummary> create(@Valid @RequestBody Task task,
                                              BindingResult result,
                                              Principal principal) {

        return save(null, task, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskSummary> update(@PathVariable(name = "id") Integer id,
                                              @Valid @RequestBody Task task,
                                              BindingResult result,
                                              Principal principal) {
        return save(id, task, result, principal);
    }

    private ResponseEntity<TaskSummary> save(Integer id, Task task, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        // get current logged creator
        User user = userService.findByEmail(principal.getName());

        task.setCreator(user);
        task.setId(id);
        TaskSummary savedTaskSummary = taskService.save(task);

        return ResponseEntity.ok(savedTaskSummary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        return null;
    }
}
