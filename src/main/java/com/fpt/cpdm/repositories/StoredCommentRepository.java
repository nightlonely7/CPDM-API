package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.CommentEntity;
import com.fpt.cpdm.entities.StoredCommentEntity;
import com.fpt.cpdm.models.storedComments.StoredCommentSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoredCommentRepository extends JpaRepository<StoredCommentEntity,Integer> {
    List<StoredCommentSummary> findAllSummaryByCommentOrderByCreatedDateDesc(CommentEntity commentEntity);
}
