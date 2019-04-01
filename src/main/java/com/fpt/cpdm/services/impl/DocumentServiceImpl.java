package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, ProjectRepository projectRepository) {
        this.documentRepository = documentRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    public Page<DocumentSummary> findAllSummary(Pageable pageable) {

        Page<DocumentSummary> documentSummaries = documentRepository.findAllSummaryBy(pageable);

        return documentSummaries;
    }

    @Override
    public DocumentSummary create(DocumentCreateForm documentCreateForm) {

        Integer projectId = documentCreateForm.getProject().getId();

        if (projectRepository.existsById(projectId) == false) {
            throw new EntityNotFoundException(projectId, "Project");
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);

        List<UserEntity> relatives = new ArrayList<>();
        if (documentCreateForm.getRelatives() != null) {
            for (IdOnlyForm idOnlyForm : documentCreateForm.getRelatives()) {
                UserEntity relative = new UserEntity(idOnlyForm.getId());
                relatives.add(relative);
            }
        }

        DocumentEntity documentEntity = new DocumentEntity();
        documentEntity.setId(null);
        documentEntity.setProject(projectEntity);
        documentEntity.setTitle(documentCreateForm.getTitle());
        documentEntity.setSummary(documentCreateForm.getSummary());
        documentEntity.setStartTime(documentCreateForm.getStartTime());
        documentEntity.setEndTime(documentCreateForm.getEndTime());
        documentEntity.setRelatives(relatives);

        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);

        DocumentSummary documentSummary = documentRepository.findSummaryById(savedDocumentEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentEntity.getId(), "Document")
        );

        return documentSummary;
    }


}
