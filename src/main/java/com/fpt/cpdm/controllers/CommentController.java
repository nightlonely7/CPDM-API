package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.comments.CommentSummary;
import com.fpt.cpdm.models.storedComments.StoredComment;
import com.fpt.cpdm.models.storedComments.StoredCommentSummary;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.CommentService;
import com.fpt.cpdm.services.StoredCommentService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final StoredCommentService storedCommentService;

    @Autowired
    public CommentController(CommentService commentService, UserService userService, StoredCommentService storedCommentService) {
        this.commentService = commentService;
        this.userService = userService;
        this.storedCommentService = storedCommentService;
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
    public ResponseEntity<Page<CommentSummary>> findByTask(@RequestParam("id") Integer id,
                                                           @PageableDefault Pageable pageable) {

        // create id only task for finding
        Task task = new Task();
        task.setId(id);

        Integer deletedStatusCode = Enum.CommentStatus.Deleted.getCommentStatusCode();
        Page<CommentSummary> commentSummaries = commentService.findAllSummaryByTaskAndStatusNot(task, pageable, deletedStatusCode);
        if (commentSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(commentSummaries);
    }

    @GetMapping("/loadOldVersion/{id}")
    public ResponseEntity<List<StoredCommentSummary>> loadOldVersion(@PathVariable(name = "id") Integer id) {

        Comment commnet = new Comment();
        commnet.setId(id);

        List<StoredCommentSummary> storedComments = storedCommentService.findAllSummaryByCommentOrderByCreatedDateDesc(commnet);

        if (storedComments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(storedComments);
    }

    @PostMapping
    public ResponseEntity<Comment> create(@Valid @RequestBody Comment comment,
                                          BindingResult result, Principal principal) {

        return save(null, comment, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> edit(@PathVariable(name = "id") Integer id,
            @Valid @RequestBody Comment comment, BindingResult result, Principal principal) {
        Comment oldComment = commentService.findById(id);
        comment.setCreatedDate(oldComment.getCreatedDate());
        if(comment.getStatus() == Enum.CommentStatus.Edited.getCommentStatusCode()){
            StoredComment storedComment = new StoredComment();
            storedComment.setContent(oldComment.getContent());
            storedComment.setComment(oldComment);
            storedCommentService.save(storedComment);
        }
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

    @DeleteMapping("/{id}")
    public ResponseEntity updateDeleteStatus(@PathVariable(name = "id") Integer id)
    {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
