package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.forms.tasks.files.TaskFileUpdateForm;
import com.fpt.cpdm.models.tasks.task_files.TaskFileDetail;
import com.fpt.cpdm.services.TaskFileService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tasks/files")
public class TaskFileController {

    private final TaskFileService taskFileService;

    @Autowired
    public TaskFileController(TaskFileService taskFileService) {
        this.taskFileService = taskFileService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskFileDetail> update(@PathVariable("id") Integer id,
                                                 @Valid TaskFileUpdateForm taskFileUpdateForm,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ModelErrorMessage.build(bindingResult);
            throw new ModelNotValidException(message);
        }

        TaskFileDetail taskFileDetail = taskFileService.update(id, taskFileUpdateForm);

        return ResponseEntity.ok(taskFileDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {

        taskFileService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
