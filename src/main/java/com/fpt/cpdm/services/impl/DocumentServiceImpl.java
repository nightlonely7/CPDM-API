package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.configurations.AuthenticationFacade;
import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.forms.documents.DocumentUpdateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.DocumentService;
import com.fpt.cpdm.utils.ModelConverter;
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
    public List<DocumentSummary> findAllSummaryByProjectId(Integer projectId) {

        String email = authenticationFacade.getAuthentication().getName();
        UserEntity relative = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        List<DocumentSummary> documentSummaries;
        if (relative.getRole().getName().equals("ROLE_ADMIN")) {
            documentSummaries = documentRepository.findAllSummaryByProject_Id(projectId);
        } else {
            documentSummaries = documentRepository.findAllSummaryByProject_IdAndRelatives(projectId, relative);
        }

        return documentSummaries;
    }

    @Override
    public Page<DocumentSummary> findAllSummary(Pageable pageable) {

        Page<DocumentSummary> documentSummaries = documentRepository.findAllSummaryByAndAvailableTrue(pageable);

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
        documentEntity.setDescription(documentCreateForm.getDescription());
        documentEntity.setStartTime(documentCreateForm.getStartTime());
        documentEntity.setEndTime(documentCreateForm.getEndTime());
        documentEntity.setRelatives(relatives);

        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);

        DocumentSummary documentSummary = documentRepository.findSummaryById(savedDocumentEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentEntity.getId(), "Document")
        );

        return documentSummary;
    }

    @Override
    public DocumentSummary update(Integer id, DocumentUpdateForm documentUpdateForm) {

        DocumentEntity documentEntity = documentRepository.findById(id).orElseThrow(
                () -> new DocumentNotFoundException(id)
        );

        Integer projectId = documentUpdateForm.getProject().getId();

        if (projectRepository.existsById(projectId) == false) {
            throw new EntityNotFoundException(projectId, "Project");
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);

        List<UserEntity> relatives = new ArrayList<>();
        if (documentUpdateForm.getRelatives() != null) {
            for (IdOnlyForm idOnlyForm : documentUpdateForm.getRelatives()) {
                UserEntity relative = new UserEntity(idOnlyForm.getId());
                relatives.add(relative);
            }
        }
        documentEntity.setProject(projectEntity);
        documentEntity.setTitle(documentUpdateForm.getTitle());
        documentEntity.setSummary(documentUpdateForm.getSummary());
        documentEntity.setDescription(documentUpdateForm.getDescription());
        documentEntity.setStartTime(documentUpdateForm.getStartTime());
        documentEntity.setEndTime(documentUpdateForm.getEndTime());
        documentEntity.setRelatives(relatives);

        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);

        DocumentSummary documentSummary = documentRepository.findSummaryById(savedDocumentEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentEntity.getId(), "Document")
        );

        return documentSummary;
    }

    @Override
    public Document deleteById(Integer id) {
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);
        if(documentEntity.isPresent()){
            documentEntity.get().setAvailable(false);
            documentRepository.save(documentEntity.get());
        }
        Document savedDepartment = ModelConverter.documentEntityToModel(documentEntity.get());
        return savedDepartment;
    }

    @Override
    public boolean existsByTitle(String title) {
        return documentRepository.existsByTitle(title);
    }

    @Override
    public Page<DocumentSummary> findAllSummaryByTitleAndSummary(String title, String summary, Pageable pageable) {
        title = title.toLowerCase();
        summary = summary.toLowerCase();
        return documentRepository
                .findAllSummaryByTitleContainingIgnoreCaseAndSummaryContainingIgnoreCaseAndAvailableTrue
                        (title, summary, pageable);
    }

}
