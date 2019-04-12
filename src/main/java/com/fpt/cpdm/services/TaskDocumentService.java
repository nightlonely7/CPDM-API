package com.fpt.cpdm.services;

import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;

import java.util.List;

public interface TaskDocumentService {

    List<DocumentSummary> readAllDocumentsByTask_Id(Integer taskId);

    List<TaskSummary> readAllTasksByDocument_Id(Integer documentId);

    List<DocumentSummary> addDocumentsToTask(List<Integer> documentIds, Integer taskId);

    List<TaskSummary> addTasksToDocument(List<Integer> taskIds, Integer documentId);

    List<DocumentSummary> deleteDocumentsFromTask(List<Integer> documentIds, Integer taskId);

    List<TaskSummary> deleteTasksFromDocument(List<Integer> taskIds, Integer documentId);

}
