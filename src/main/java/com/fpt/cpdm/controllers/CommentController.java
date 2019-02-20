package com.fpt.cpdm.controllers;


import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.services.CommentService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> readAll() {

        List<Comment> comments = commentService.findAll();
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody Comment comment,
                                           BindingResult result) {

        return save(null, comment, result);
    }

    private ResponseEntity<Comment> save(Integer id, Comment comment, BindingResult result) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        comment.setId(id);
        Comment savedComment = commentService.save(comment);

        return ResponseEntity.ok(savedComment);
    }
}
