package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.forms.documents.DocumentUpdateForm;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface DocumentService {

    List<DocumentSummary> findAllSummaryByProjectId(Integer projectId);

    Page<DocumentSummary> findAllSummary(Pageable pageable);

    DocumentDetail findDetailById(Integer id);

    Page<DocumentSummary> findAllSummaryByRelatives(Pageable pageable);

    DocumentSummary create(DocumentCreateForm documentCreateForm, boolean selectAll, List<Integer> departmentList);

    DocumentSummary update(Integer id, DocumentUpdateForm documentUpdateForm);

    Document deleteById(Integer id);

    boolean existsByTitle(String title);

    Page<DocumentSummary> findAllSummaryByTitleAndSummary(String title, String summary, Pageable pageable);

}
