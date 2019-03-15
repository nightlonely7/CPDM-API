package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.forms.tasks.issues.TaskIssueForm;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueDetail;
import com.fpt.cpdm.services.TaskIssueService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/taskIssues")
public class TaskIssueController {

    private final TaskIssueService taskIssueService;

    @Autowired
    public TaskIssueController(TaskIssueService taskIssueService) {
        this.taskIssueService = taskIssueService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskIssueDetail> update(@PathVariable("id") Integer id,
                                                  @Valid @RequestBody TaskIssueForm taskIssueForm,
                                                  BindingResult result) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        TaskIssueDetail taskIssueDetail = taskIssueService.update(id, taskIssueForm);

        return ResponseEntity.ok(taskIssueDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {

        taskIssueService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
