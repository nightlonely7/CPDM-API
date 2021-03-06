package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.forms.tasks.TaskCreateForm;
import com.fpt.cpdm.forms.tasks.TaskSearchForm;
import com.fpt.cpdm.forms.tasks.TaskUpdateForm;
import com.fpt.cpdm.forms.tasks.files.TaskFileCreateForm;
import com.fpt.cpdm.forms.tasks.issues.TaskIssueForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.tasks.histories.TaskHistorySummary;
import com.fpt.cpdm.models.tasks.task_files.TaskFileDetail;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueDetail;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueStatus;
import com.fpt.cpdm.models.users.UserSummary;
import com.fpt.cpdm.services.*;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final TaskFileService taskFileService;
    private final TaskIssueService taskIssueService;
    private final TaskRelativeService taskRelativeService;
    private final TaskDocumentService taskDocumentService;
    private final TaskHistoryService taskHistoryService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService,
                          FileStorageService fileStorageService,
                          TaskFileService taskFileService,
                          TaskIssueService taskIssueService,
                          TaskRelativeService taskRelativeService,
                          TaskDocumentService taskDocumentService, TaskHistoryService taskHistoryService) {
        this.taskService = taskService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.taskFileService = taskFileService;
        this.taskIssueService = taskIssueService;
        this.taskRelativeService = taskRelativeService;
        this.taskDocumentService = taskDocumentService;
        this.taskHistoryService = taskHistoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDetail> readById(@PathVariable(name = "id") Integer id) {

        TaskDetail taskDetail = taskService.findDetailById(id);

        return ResponseEntity.ok(taskDetail);
    }

    @GetMapping("/search/executes")
    public ResponseEntity<Page<TaskSummary>> readByExecutes(TaskSearchForm taskSearchForm,
                                                            @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutor(taskSearchForm, pageable);

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/all/executes")
    public ResponseEntity<List<TaskSummary>> readAllByExecutes() {
        List<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutor();

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("search/executes/notComplete")
    public ResponseEntity<Page<TaskSummary>> findAllByExecutorAndDateRangeAndStatus(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                    @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                                    @PageableDefault Pageable pageable) {
        LocalDateTime fromTime = fromDate.atStartOfDay();
        LocalDateTime toTime = toDate.atTime(23, 59, 59);

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutorAndDateRangeAndNotComplete(fromTime, toTime, pageable);

        if (taskSummaries.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);

    }

    @GetMapping("search/executes/all-working")
    public ResponseEntity<Page<TaskSummary>> findAllByExecutorStatus(@PathVariable(name = "status") String status,
                                                                     @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutorAndStatus(status, pageable);

        if (taskSummaries.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);

    }


    @GetMapping("/search/executes/notAssigned")
    public ResponseEntity<Page<TaskSummary>> findAllByExecutorAndDateRangeAndNotAssigned(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                         @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                                         @PageableDefault Pageable pageable) {
        LocalDateTime fromTime = fromDate.atStartOfDay();
        LocalDateTime toTime = toDate.atTime(23, 59, 59);

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutorAndDateRangeAndNotAssigned(fromTime, toTime, pageable);

        if (taskSummaries.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/executes/fullAssigned")
    public ResponseEntity<Page<TaskSummary>> findAllByExecutorAndDateRangeAndFullAssigned(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                          @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                                          @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutorAndDateRangeAndFullAssigned(fromDate, toDate, pageable);

        if (taskSummaries.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/executes/partAssigned")
    public ResponseEntity<Page<TaskSummary>> findAllByExecutorAndDateRangeAndPartAssigned(@RequestParam(name = "fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                                                          @RequestParam(name = "toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                                                          @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByExecutorAndDateRangeAndPartAssigned(fromDate, toDate, pageable);

        if (taskSummaries.getContent().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskSummaries);
    }


    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @GetMapping("/search/creates")
    public ResponseEntity<Page<TaskSummary>> readByCreates(TaskSearchForm taskSearchForm,
                                                           @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByCreator(taskSearchForm, pageable);

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/all/creates")
    public ResponseEntity<List<TaskSummary>> readAllByCreates() {
        List<TaskSummary> taskSummaries = taskService.findAllSummaryByCreator();

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/relatives")
    public ResponseEntity<Page<TaskSummary>> readByRelatives(TaskSearchForm taskSearchForm,
                                                             @PageableDefault Pageable pageable) {

        Page<TaskSummary> taskSummaries = taskService.findAllSummaryByRelatives(taskSearchForm, pageable);

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/all/relatives")
    public ResponseEntity<List<TaskSummary>> readAllByRelatives() {
        List<TaskSummary> taskSummaries = taskService.findAllSummaryByRelatives();

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/{id}/relatives")
    public ResponseEntity<List<UserSummary>> readAllRelatives(@PathVariable("id") Integer id) {

        List<UserSummary> userSummaries = taskRelativeService.readAll(id);
        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userSummaries);
    }

    @PostMapping("/{id}/relatives")
    public ResponseEntity<List<UserSummary>> addRelatives(
            @PathVariable("id") Integer id,
            @RequestBody List<IdOnlyForm> relatives) {

        List<UserSummary> userSummaries = taskRelativeService.add(id, relatives);
        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userSummaries);
    }

    @DeleteMapping("/{id}/relatives/{userId}")
    public ResponseEntity<List<UserSummary>> deleteRelatives(
            @PathVariable("id") Integer id,
            @PathVariable("userId") Integer userId) {

        taskRelativeService.delete(id, userId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/documents")
    public ResponseEntity<List<DocumentSummary>> readAllDocuments(@PathVariable("id") Integer id) {

        List<DocumentSummary> documentSummaries = taskDocumentService.readAllDocumentsByTask_Id(id);

        return ResponseEntity.ok(documentSummaries);
    }

    @PutMapping("/{id}/documents/{documentIds}")
    public ResponseEntity<List<DocumentSummary>> addDocuments(@PathVariable("id") Integer taskId,
                                                              @PathVariable("documentIds") List<Integer> documentIds) {

        List<DocumentSummary> documentSummaries = taskDocumentService.addDocumentsToTask(documentIds, taskId);

        return ResponseEntity.ok(documentSummaries);
    }

    @DeleteMapping("/{id}/documents/{documentIds}")
    public ResponseEntity<List<DocumentSummary>> deleteDocuments(@PathVariable("id") Integer taskId,
                                                                 @PathVariable("documentIds") List<Integer> documentIds) {

        List<DocumentSummary> documentSummaries = taskDocumentService.deleteDocumentsFromTask(documentIds, taskId);

        return ResponseEntity.ok(documentSummaries);
    }

    @GetMapping("/{id}/files")
    public ResponseEntity<List<TaskFileDetail>> loadFiles(@PathVariable("id") Integer id) {

        List<TaskFileDetail> taskFilesSummaries = taskFileService.findAllSummaryByTask_Id(id);

        return ResponseEntity.ok(taskFilesSummaries);
    }

    @PostMapping("/{id}/files")
    public ResponseEntity<TaskFileDetail> uploadFile(@PathVariable("id") Integer taskId,
                                                     @Valid TaskFileCreateForm taskFileCreateForm,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String message = ModelErrorMessage.build(bindingResult);
            throw new ModelNotValidException(message);
        }

        TaskFileDetail taskFileDetail = taskFileService.create(taskId, taskFileCreateForm);

        return ResponseEntity.ok(taskFileDetail);
    }

    @GetMapping("/{id}/issues")
    public ResponseEntity<List<TaskIssueDetail>> readAllIssue(@PathVariable("id") Integer taskId) {

        List<TaskIssueDetail> taskIssueDetails = taskIssueService.readAll(taskId);
        if (taskIssueDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(taskIssueDetails);
    }

    @GetMapping("/{id}/issues/status")
    public ResponseEntity<TaskIssueStatus> readIssueStatus(@PathVariable("id") Integer id) {

        TaskIssueStatus taskIssueStatus = taskService.findIssueStatusById(id);

        return ResponseEntity.ok(taskIssueStatus);
    }

    @PostMapping("/{id}/issues")
    public ResponseEntity<TaskIssueDetail> createIssue(@PathVariable("id") Integer taskId,
                                                       @Valid @RequestBody TaskIssueForm taskIssueForm,
                                                       BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        TaskIssueDetail taskIssueDetail = taskIssueService.create(taskId, taskIssueForm);

        return ResponseEntity.ok(taskIssueDetail);
    }

    @GetMapping("/{id}/histories")
    public ResponseEntity<List<TaskHistorySummary>> readAllHistoryByTask_Id(@PathVariable("id") Integer taskId) {

        List<TaskHistorySummary> taskHistorySummaries = taskHistoryService.findAllSummaryByTask_Id(taskId);

        return ResponseEntity.ok(taskHistorySummaries);
    }

    @PostMapping
    public ResponseEntity<TaskDetail> create(@Valid @RequestBody TaskCreateForm taskCreateForm,
                                             BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        TaskDetail taskDetail = taskService.create(taskCreateForm);

        return ResponseEntity.ok(taskDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDetail> update(@PathVariable(name = "id") Integer id,
                                             @Valid @RequestBody TaskUpdateForm taskUpdateForm,
                                             BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        TaskDetail taskDetail = taskService.update(id, taskUpdateForm);


        return ResponseEntity.ok(taskDetail);
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskSummary> complete(@PathVariable("id") Integer id) {

        TaskSummary savedTaskSummary = taskService.complete(id);

        return ResponseEntity.ok(savedTaskSummary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/childs")
    public ResponseEntity childTask(@PathVariable(name = "id") Integer id,
                                    @PageableDefault Pageable pageable) {
        Page<TaskSummary> taskSummaries = taskService.findAllByParentTask_Id(id, pageable);

        return ResponseEntity.ok(taskSummaries);
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    @GetMapping("/search/basicByExecutes")
    public ResponseEntity<List<TaskSummary>> getBasicByExecute(@RequestParam("projectId") Integer projectId) {
        List<TaskSummary> taskSummaries = taskService.findAllBasicByCurrentExecutorAndProject_Id(projectId);
        return ResponseEntity.ok(taskSummaries);
    }

}
