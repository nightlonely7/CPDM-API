package com.fpt.cpdm.services;

import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSumary;
import com.fpt.cpdm.models.tasks.Task;

import java.util.List;

public interface CommentService extends CRUDService<Comment>{
    List<CommentSumary> findAllByTask(Task task);
}
