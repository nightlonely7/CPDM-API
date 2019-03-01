package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

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

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetail> readById(@PathVariable(name = "id") Integer id, Principal principal) {

        // get current logged executor
        User user = userService.findByEmail(principal.getName());

        TaskDetail taskDetail = taskService.findDetailById(user, id);

        return ResponseEntity.ok(taskDetail);
    }

    @GetMapping("/findByCurrentLoggedExecutor")
    public ResponseEntity<Page<TaskSummary>> findByCurrentLoggedExecutor(
            @PageableDefault Pageable pageable,
            Principal principal) {

        // get current logged executor
        User user = userService.findByEmail(principal.getName());

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutor(user, pageable);
        if (taskSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/findByCurrentLoggedCreator")
    public ResponseEntity<Page<TaskSummary>> findByTitleAndCurrentLoggedCreator(
            @RequestParam(value = "title", required = false, defaultValue = "") String title,
            @RequestParam(value = "summary", required = false, defaultValue = "") String summary,
            @PageableDefault Pageable pageable,
            Principal principal) {

        // get current logged creator
        User user = userService.findByEmail(principal.getName());

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByCreator(user, title, summary, pageable);
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

    @PatchMapping("/{id}/done")
    public ResponseEntity taskDone(@PathVariable("id") Integer id, Principal principal) {

        // get current logged executor
        User executor = userService.findByEmail(principal.getName());

        // create [id, executor, and status only] task for updating
        Task task = new Task();
        task.setId(id);
        task.setExecutor(executor);
        task.setStatus("Done");

        TaskSummary savedTaskSummary = taskService.changeStatus(task);

        return ResponseEntity.ok(savedTaskSummary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
