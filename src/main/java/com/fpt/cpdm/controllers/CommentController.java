package com.fpt.cpdm.controllers;


import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.CommentService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> readAll() {

        List<Comment> comments = commentService.findAll();
        if (comments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(comments);
    }

    @GetMapping("/findByTask")
    public ResponseEntity<List<CommentSummary>> findByTask(@RequestParam("id") Integer id) {

        // create id only task for finding
        Task task = new Task();
        task.setId(id);

        List<CommentSummary> commentSummaries = commentService.findAllSummaryByTask(task);
        if (commentSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(commentSummaries);
    }

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody Comment comment,
                                          BindingResult result, Principal principal) {

        return save(null, comment, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> edit(@PathVariable(name = "id") Integer id,
            @Valid @RequestBody Comment comment, BindingResult result, Principal principal) {

        comment.setCreatedDate(commentService.findById(id).getCreatedDate());
        return save( id, comment, result, principal);
    }

    private ResponseEntity<Comment> save(Integer id, Comment comment, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        User user = userService.findByEmail(principal.getName());

        comment.setId(id);
        comment.setUser(user);
        Comment savedComment = commentService.save(comment);

        return ResponseEntity.ok(savedComment);
    }
}
