package com.fpt.cpdm.models.tasks;

import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.documents.DocumentSummary;

import java.util.List;

public interface TaskDetail extends TaskSummary {

    String getDescription();

    List<DocumentSummary> getDocuments();

}
