package com.fpt.cpdm.models.storedComments;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.IdOnly;
import java.time.LocalDateTime;

public interface StoredCommentSummary {

    String getContent();

    LocalDateTime getCreatedDate();

    IdOnly getComment();

}
