package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.CommentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {

    List<CommentSummary> findAllSummaryByTaskAndStatusNot(TaskEntity taskEntity, Integer status);

    Page<CommentSummary> findAllSummaryByTaskAndStatusNot(TaskEntity taskEntity, Pageable pageable,Integer status);

    List<CommentSummary> findAllSummaryByParentCommentId(Integer parentCommentId);

}
