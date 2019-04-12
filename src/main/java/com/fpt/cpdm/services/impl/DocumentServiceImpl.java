package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.configurations.AuthenticationFacade;
import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, UserRepository userRepository, ProjectRepository projectRepository, AuthenticationFacade authenticationFacade) {
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public Page<DocumentSummary> findAllSummary(Pageable pageable) {

        Page<DocumentSummary> documentSummaries = documentRepository.findAllSummaryBy(pageable);

        return documentSummaries;
    }

    @Override
    public DocumentDetail findDetailById(Integer id) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity relative = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        DocumentDetail documentDetail = documentRepository.findDetailByIdAndAvailableTrue(id).orElseThrow(
                () -> new DocumentNotFoundException(id)
        );

        if (relative.getRole().getName().equals("ROLE_ADMIN") == false &&
                documentRepository.existsByIdAndRelativesAndAvailableTrue(id, relative) == false) {
            throw new UnauthorizedException();
        }

        return documentDetail;
    }

    @Override
    public Page<DocumentSummary> findAllSummaryByRelatives(Pageable pageable) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity relative = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        Page<DocumentSummary> documentSummaries = documentRepository.findAllSummaryByRelatives(relative, pageable);

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
