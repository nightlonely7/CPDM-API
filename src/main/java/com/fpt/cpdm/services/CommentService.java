package com.fpt.cpdm.services;

import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.tasks.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentService extends CRUDService<Comment> {

    Page<CommentSummary> findAllSummaryByTaskAndStatusNot(Task task, Pageable pageable, Integer status);

    List<CommentSummary> findAllSummaryByParentCommentId(Integer parentCommentId);
}
