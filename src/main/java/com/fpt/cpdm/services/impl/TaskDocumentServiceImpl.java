package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.exceptions.ConflictException;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.services.TaskDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskDocumentServiceImpl implements TaskDocumentService {

    private final TaskRepository taskRepository;
    private final DocumentRepository documentRepository;

    @Autowired
    public TaskDocumentServiceImpl(TaskRepository taskRepository, DocumentRepository documentRepository) {
        this.taskRepository = taskRepository;
        this.documentRepository = documentRepository;
    }


    @Override
    public List<DocumentSummary> readAllDocumentsByTask_Id(Integer taskId) {

        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(taskId, "Task")
        );
        List<DocumentSummary> documentSummaries = documentRepository.findAllSummaryByTasks(taskEntity);

        return documentSummaries;
    }

    @Override
    public List<TaskSummary> readAllTasksByDocument_Id(Integer documentId) {

        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(
                () -> new EntityNotFoundException(documentId, "Document")
        );
        List<TaskSummary> taskSummaries = taskRepository.findAllSummaryByDocuments(documentEntity);

        return taskSummaries;
    }

    @Override
    public List<DocumentSummary> addDocumentsToTask(List<Integer> documentIds, Integer taskId) {

        // find the task to add documents into
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(taskId, "Task")
        );

        // find all current documents in the task
        List<DocumentEntity> currentDocumentEntities = documentRepository.findAllByTasks(taskEntity);

        List<Integer> currentDocumentIds = currentDocumentEntities
                .stream().map(DocumentEntity::getId).collect(Collectors.toList());

        for (Integer newDocumentId : documentIds) {
            if (currentDocumentIds.contains(newDocumentId) == false) {
                DocumentEntity newDocumentEntity = documentRepository.findById(newDocumentId).orElseThrow(
                        () -> new EntityNotFoundException(newDocumentId, "Document")
                );
                currentDocumentEntities.add(newDocumentEntity);
            }
        }

        taskEntity.setDocuments(currentDocumentEntities);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        List<DocumentSummary> savedDocumentSummaries = documentRepository.findAllSummaryByTasks(savedTaskEntity);

        return savedDocumentSummaries;
    }

    @Override
    public List<TaskSummary> addTasksToDocument(List<Integer> taskIds, Integer documentId) {

        // find the document to add tasks into
        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(
                () -> new EntityNotFoundException(documentId, "Document")
        );

        // find all current tasks in the document
        List<TaskEntity> currentTaskEntities = taskRepository.findAllByDocuments(documentEntity);

        List<Integer> currentTaskIds = currentTaskEntities
                .stream().map(TaskEntity::getId).collect(Collectors.toList());

        for (Integer newTaskId : taskIds) {
            if (currentTaskIds.contains(newTaskId) == false) {
                TaskEntity newTaskEntity = taskRepository.findById(newTaskId).orElseThrow(
                        () -> new EntityNotFoundException(newTaskId, "Task")
                );
                currentTaskEntities.add(newTaskEntity);
            }
        }
        documentEntity.setTasks(currentTaskEntities);
        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);
        List<TaskSummary> savedTaskSummaries = taskRepository.findAllSummaryByDocuments(savedDocumentEntity);

        return savedTaskSummaries;
    }

    @Override
    public List<DocumentSummary> deleteDocumentsFromTask(List<Integer> documentIds, Integer taskId) {

        // find the task to delete documents from
        TaskEntity taskEntity = taskRepository.findById(taskId).orElseThrow(
                () -> new EntityNotFoundException(taskId, "Task")
        );

        // find all current documents in the task
        List<DocumentEntity> currentDocumentEntities = documentRepository.findAllByTasks(taskEntity);

        List<Integer> currentDocumentIds = currentDocumentEntities
                .stream().map(DocumentEntity::getId).collect(Collectors.toList());

        // delete document from tasks
        for (Integer deletedDocumentId : documentIds) {

            // check if document id is not in current documents
            if (currentDocumentIds.contains(deletedDocumentId) == false) {
                throw new ConflictException("The document with id '" + deletedDocumentId + "' is not exists in " +
                        "the task with id '" + taskEntity.getId());
            }
            DocumentEntity deletedDocumentEntity = documentRepository.findById(deletedDocumentId).orElseThrow(
                    () -> new EntityNotFoundException(deletedDocumentId, "Document")
            );
            currentDocumentEntities.remove(deletedDocumentEntity);
        }

        taskEntity.setDocuments(currentDocumentEntities);
        TaskEntity savedTaskEntity = taskRepository.save(taskEntity);
        List<DocumentSummary> savedDocumentSummaries = documentRepository.findAllSummaryByTasks(savedTaskEntity);

        return savedDocumentSummaries;
    }

    @Override
    public List<TaskSummary> deleteTasksFromDocument(List<Integer> taskIds, Integer documentId) {

        // find the document to delete tasks from
        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(
                () -> new EntityNotFoundException(documentId, "Document")
        );

        // find all current tasks in the document
        List<TaskEntity> currentTaskEntities = taskRepository.findAllByDocuments(documentEntity);

        List<Integer> currentTaskIds = currentTaskEntities
                .stream().map(TaskEntity::getId).collect(Collectors.toList());

        // delete task from documents
        for (Integer deletedTaskId : taskIds) {

            // check if task id is not in current tasks
            if (currentTaskIds.contains(deletedTaskId) == false) {
                throw new ConflictException("The task with id '" + deletedTaskId + "' is not exists in " +
                        "the document with id '" + documentEntity.getId());
            }
            TaskEntity deletedTaskEntity = taskRepository.findById(deletedTaskId).orElseThrow(
                    () -> new EntityNotFoundException(deletedTaskId, "Task")
            );
            currentTaskEntities.remove(deletedTaskEntity);
        }

        documentEntity.setTasks(currentTaskEntities);
        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);
        List<TaskSummary> savedTaskSummaries = taskRepository.findAllSummaryByDocuments(savedDocumentEntity);

        return savedTaskSummaries;
    }

}
