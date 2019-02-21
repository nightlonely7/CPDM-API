package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.CommentEntity;
import com.fpt.cpdm.exceptions.comments.CommentNotFoundException;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.repositories.CommentRepository;
import com.fpt.cpdm.services.CommentService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Comment save(Comment comment) {
        // check id exist
        if (comment.getId() != null && commentRepository.existsById(comment.getId()) == false) {
            throw new CommentNotFoundException(comment.getId());
        }

        CommentEntity commentEntity = ModelConverter.commentModelToEntity(comment);
        CommentEntity savedcommentEntity = commentRepository.save(commentEntity);
        Comment savedcomment = ModelConverter.commentEntityToModel(savedcommentEntity);

        return savedcomment;
    }

    @Override
    public List<Comment> saveAll(List<Comment> models) {
        return null;
    }

    @Override
    public Comment findById(Integer id) {
        return null;
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
    public List<CommentSummary> findAllByTask_Id(Integer taskId) {
        List<CommentSummary> commentSumaries = commentRepository.findAllByTask_Id(taskId);
        return commentSumaries;
    }
}
