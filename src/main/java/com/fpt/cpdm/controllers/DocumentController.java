package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.forms.documents.DocumentUpdateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.services.DocumentService;
import com.fpt.cpdm.services.TaskDocumentService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final TaskDocumentService taskDocumentService;

    @Autowired
    public DocumentController(DocumentService documentService, TaskDocumentService taskDocumentService) {
        this.documentService = documentService;
        this.taskDocumentService = taskDocumentService;
    }

//    @GetMapping
//    public ResponseEntity<List<Document>> readAll() {
//
//        List<Document> documents = documentService.findAll();
//        if (documents.isEmpty()) {
//            return ResponseEntity.noContent().build(); //phản hồi trạng thái noContent
//        }
//
//        return ResponseEntity.ok(documents);
//    }

    @GetMapping
    public ResponseEntity<Page<DocumentSummary>> readAll(@PageableDefault Pageable pageable) {

        Page<DocumentSummary> documentSummaries = documentService.findAllSummary(pageable);

        return ResponseEntity.ok(documentSummaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetail> readById(@PathVariable("id") Integer id) {

        DocumentDetail documentDetail = documentService.findDetailById(id);

        return ResponseEntity.ok(documentDetail);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<DocumentSummary>> readAll(@RequestParam("projectId") Integer projectId) {

        List<DocumentSummary> documentSummaries = documentService.findAllSummaryByProjectId(projectId);

        return ResponseEntity.ok(documentSummaries);
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskSummary>> readAllTasks(@PathVariable("id") Integer id) {

        List<TaskSummary> taskSummaries = taskDocumentService.readAllTasksByDocument_Id(id);

        return ResponseEntity.ok(taskSummaries);
    }

    @PostMapping("/{id}/tasks")
    public ResponseEntity<List<TaskSummary>> addTasks(@PathVariable("id") Integer documentId,
                                                      @RequestBody List<IdOnlyForm> tasks) {

        List<Integer> taskIds = tasks.stream().map(IdOnlyForm::getId).collect(Collectors.toList());
        List<TaskSummary> taskSummaries = taskDocumentService.addTasksToDocument(taskIds, documentId);

        return ResponseEntity.ok(taskSummaries);
    }

    @DeleteMapping("/{id}/tasks")
    public ResponseEntity<List<TaskSummary>> deleteTasks(@PathVariable("id") Integer documentId,
                                                         @RequestBody List<IdOnlyForm> tasks) {

        List<Integer> taskIds = tasks.stream().map(IdOnlyForm::getId).collect(Collectors.toList());
        List<TaskSummary> taskSummaries = taskDocumentService.deleteTasksFromDocument(taskIds, documentId);

        return ResponseEntity.ok(taskSummaries);
    }

    @GetMapping("/search/relatives")
    public ResponseEntity<Page<DocumentSummary>> findByRelatives(@PageableDefault Pageable pageable) {

        Page<DocumentSummary> documentSummaries = documentService.findAllSummaryByRelatives(pageable);

        return ResponseEntity.ok(documentSummaries);
    }


    @PostMapping
    public ResponseEntity<DocumentSummary> create(@Valid @RequestBody DocumentCreateForm documentCreateForm,
                                                  BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        DocumentSummary documentSummary = documentService.create(documentCreateForm);

        return ResponseEntity.ok(documentSummary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentSummary> update(@PathVariable(name = "id") Integer id,
                                           @Valid @RequestBody DocumentUpdateForm documentUpdateForm,
                                           BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        DocumentSummary documentSummary = documentService.update(id, documentUpdateForm);

        return ResponseEntity.ok(documentSummary);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable(name = "id") Integer id) {
        documentService.deleteById(id);
    }


    @GetMapping("/check/existByTitle")
    public ResponseEntity<Boolean> existByName(@RequestParam("title") String title){
        return ResponseEntity.ok(documentService.existsByTitle(title));
    }

    @GetMapping("/search/titleAndSummary")
    public ResponseEntity<Page<DocumentSummary>> findByTitleAndSummary(@RequestParam("title") String title,
                                                                       @RequestParam("summary") String summary,
                                                                       @PageableDefault Pageable pageable) {
        Page<DocumentSummary> documentSummaries =
                documentService.findAllSummaryByTitleAndSummary(title, summary, pageable);
        if (documentSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(documentSummaries);
    }

}
