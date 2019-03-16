package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.UploadFileResponse;
import com.fpt.cpdm.models.taskFiles.TaskFilesSummary;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskCreateForm;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.FileStorageService;
import com.fpt.cpdm.services.TaskFilesService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final TaskFilesService taskFilesService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService, FileStorageService fileStorageService, TaskFilesService taskFilesService) {
        this.taskService = taskService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.taskFilesService = taskFilesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetail> readById(@PathVariable(name = "id") Integer id, Principal principal) {

        // get current logged executor
        User user = userService.findByEmail(principal.getName());

        TaskDetail taskDetail = taskService.findDetailById(user, id);

        return ResponseEntity.ok(taskDetail);
    }

    @GetMapping("/findAllSummaryByTitle/search")
    public ResponseEntity<Page<TaskSummary>> findAllSummaryByTitle(@RequestParam(name = "title") String title,
                                               @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByTitle(title, pageable);

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/advancedSearch/search")
    public ResponseEntity<Page<TaskSummary>> findSummaryByTitleAndSummaryAndDescriptionContaining(
            @RequestParam(name = "searchValue") String searchValue, @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findSummaryByTitleAndSummaryAndDescriptionContaining(
                searchValue, searchValue, searchValue, pageable);

        return ResponseEntity.ok(taskSummaries);
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

    @GetMapping("/search/relatives")
    public ResponseEntity<Page<TaskSummary>> relatives(@PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByRelatives(pageable);
        if (taskSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<TaskFilesSummary>> loadFiles(@PathVariable("id") Integer id) {

        List<TaskFilesSummary> taskFilesSummaries = taskFilesService.findSummaryByTask_Id(id);
        if (taskFilesSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskFilesSummaries);
    }

    @PostMapping("/{id}/uploadFile")
    public ResponseEntity<UploadFileResponse> uploadFile(
            @PathVariable("id") Integer id,
            @RequestParam("file") MultipartFile file) {

        // store the file
        String filename = fileStorageService.store(file);

        // set filename for task
        if (filename.trim().isEmpty() == false) {
            taskService.uploadFile(id, filename);
        }

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(filename)
                .toUriString();
        UploadFileResponse uploadFileResponse = new UploadFileResponse(filename, fileDownloadUri,
                file.getContentType(), file.getSize());

        return ResponseEntity.ok(uploadFileResponse);
    }

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody TaskCreateForm taskCreateForm,
                                 BindingResult result,
                                 Principal principal) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        TaskSummary taskSummary = taskService.create(taskCreateForm);


        return ResponseEntity.ok(taskSummary);
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
