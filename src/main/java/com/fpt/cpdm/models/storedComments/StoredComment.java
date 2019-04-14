package com.fpt.cpdm.models.storedComments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.comments.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class StoredComment {
    @EqualsAndHashCode.Include
    private Integer id;

    private String content;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdDate;

    @NotNull
    private Comment comment;
}
