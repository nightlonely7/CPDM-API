package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.CommentEntity;
import com.fpt.cpdm.entities.StoredCommentEntity;
import com.fpt.cpdm.exceptions.comments.CommentNotFoundException;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.storedComments.StoredComment;
import com.fpt.cpdm.models.storedComments.StoredCommentSummary;
import com.fpt.cpdm.repositories.CommentRepository;
import com.fpt.cpdm.repositories.StoredCommentRepository;
import com.fpt.cpdm.services.StoredCommentService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoredCommentServiceImpl implements StoredCommentService {

    private final StoredCommentRepository storedCommentRepository;
    private final CommentRepository commentRepository;

    public StoredCommentServiceImpl(StoredCommentRepository storedCommentRepository, CommentRepository commentRepository) {
        this.storedCommentRepository = storedCommentRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public List<StoredCommentSummary> findAllSummaryByCommentOrderByCreatedDateDesc(Comment comment) {
        CommentEntity commentEntity = ModelConverter.commentModelToEntity(comment);
        return storedCommentRepository.findAllSummaryByCommentOrderByCreatedDateDesc(commentEntity);
    }

    @Override
    public StoredComment save(StoredComment model) {
        // check comment exists (can be null)
        if (commentRepository.existsById(model.getComment().getId()) == false) {
            throw new CommentNotFoundException(model.getId());
        }

        StoredCommentEntity storedCommentEntity = ModelConverter.storedCommentModelToEntity(model);
        StoredCommentEntity savedStoredCommentEntity = storedCommentRepository.save(storedCommentEntity);
        StoredComment savedStoredComment = ModelConverter.storedCommentEntityToModel(savedStoredCommentEntity);

        return savedStoredComment;
    }

    @Override
    public List<StoredComment> saveAll(List<StoredComment> models) {
        return null;
    }

    @Override
    public StoredComment findById(Integer id) {
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<StoredComment> findAll() {
        return null;
    }

    @Override
    public List<StoredComment> findAllById(List<Integer> ids) {
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
    public void delete(StoredComment model) {

    }

    @Override
    public void deleteAll(List<StoredComment> models) {

    }

    @Override
    public void deleteAll() {

    }
}
