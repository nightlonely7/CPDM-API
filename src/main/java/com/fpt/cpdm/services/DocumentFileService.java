package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.documents.files.DocumentFileCreateForm;
import com.fpt.cpdm.forms.documents.files.DocumentFileUpdateForm;
import com.fpt.cpdm.models.documents.document_files.DocumentFileDetail;

import java.util.List;

public interface DocumentFileService {

    DocumentFileDetail create(Integer id, DocumentFileCreateForm documentCreateForm);

    DocumentFileDetail update(Integer id, DocumentFileUpdateForm documentFileUpdateForm);

    List<DocumentFileDetail> findAllDetailByDocument_Id(Integer id);

    void delete(Integer id);

}
