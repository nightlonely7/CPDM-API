package com.fpt.cpdm.services;

import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.storedComments.StoredComment;
import com.fpt.cpdm.models.storedComments.StoredCommentSummary;

import java.util.List;

public interface StoredCommentService extends CRUDService<StoredComment> {
    List<StoredCommentSummary> findAllSummaryByCommentOrderByCreatedDateDesc(Comment comment);
}
