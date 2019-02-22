package com.fpt.cpdm.services;

import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.tasks.Task;

import java.util.List;

public interface CommentService extends CRUDService<Comment> {

    List<CommentSummary> findAllSummaryByTask(Task task);
}
