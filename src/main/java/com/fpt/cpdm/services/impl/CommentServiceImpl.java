package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.CommentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.exceptions.comments.CommentNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.repositories.CommentRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.CommentService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Comment save(Comment comment) {

        // check comment exists (can be null)
        if (comment.getId() != null && commentRepository.existsById(comment.getId()) == false) {
            throw new CommentNotFoundException(comment.getId());
        }

        // check user exists
        if (userRepository.existsById(comment.getUser().getId()) == false) {
            throw new UserNotFoundException(comment.getUser().getId());
        }

        // check task exists
        if (taskRepository.existsById(comment.getTask().getId()) == false) {
            throw new TaskNotFoundException(comment.getTask().getId());
        }

        CommentEntity commentEntity = ModelConverter.commentModelToEntity(comment);
        CommentEntity savedCommentEntity = commentRepository.save(commentEntity);
        Comment savedComment = ModelConverter.commentEntityToModel(savedCommentEntity);

        return savedComment;
    }

    @Override
    public List<Comment> saveAll(List<Comment> models) {
        return null;
    }

    @Override
    public Comment findById(Integer id) {
        CommentEntity commentEntity = commentRepository.findById(id).orElseThrow(
                () -> new CommentNotFoundException(id)
        );
        return ModelConverter.commentEntityToModel(commentEntity);
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<Comment> findAll() {
        return null;
    }

    @Override
    public List<Comment> findAllById(List<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void delete(Comment model) {

    }

    @Override
    public void deleteAll(List<Comment> models) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Page<CommentSummary> findAllSummaryByTaskAndStatusNot(Task task, Pageable pageable, Integer status){

        TaskEntity taskEntity = ModelConverter.taskModelToEntity(task);
        Page<CommentSummary> commentSummaries = commentRepository.findAllSummaryByTaskAndStatusNot(taskEntity, pageable, status);
        return commentSummaries;
    }

    @Override
    public List<CommentSummary> findAllSummaryByParentCommentId(Integer parentCommentId) {
        List<CommentSummary> commentSummaries = commentRepository.findAllSummaryByParentCommentId(parentCommentId);

        return commentSummaries;
    }
}
