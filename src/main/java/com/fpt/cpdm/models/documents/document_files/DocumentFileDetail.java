package com.fpt.cpdm.models.documents.document_files;

import com.fpt.cpdm.models.NameIdOnly;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.users.UserDisplayName;

import java.time.LocalDateTime;

public interface DocumentFileDetail {

    Integer getId();
    DocumentSummary getDocument();
    String getFilename();
    String getDetailFilename();
    String getExtension();
    String getDescription();
    UserDisplayName getCreator();
    LocalDateTime getCreatedTime();
    UserDisplayName getLastEditor();
    LocalDateTime getLastModifiedTime();

}
