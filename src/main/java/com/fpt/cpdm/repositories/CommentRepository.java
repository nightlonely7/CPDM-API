package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.CommentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.models.comments.CommentSumary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Integer> {
    List<CommentSumary> findAllByTask(TaskEntity task);
}
