package com.fpt.cpdm.services;

import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;

import java.util.List;

public interface CommentService extends CRUDService<Comment>{
    List<CommentSummary> findAllByTask_Id(Integer taskId);
}
