package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.configurations.AuthenticationFacade;
import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.BadRequestException;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.forms.documents.DocumentCreateForm;
import com.fpt.cpdm.forms.documents.DocumentSearchForm;
import com.fpt.cpdm.forms.documents.DocumentUpdateForm;
import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.documents.DocumentDetail;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.users.UserSummary;
import com.fpt.cpdm.repositories.DocumentHistoryRepository;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.DocumentHistoryService;
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
    private final DocumentHistoryService documentHistoryService;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, DocumentHistoryService documentHistoryService, UserRepository userRepository, ProjectRepository projectRepository, AuthenticationFacade authenticationFacade) {
        this.documentRepository = documentRepository;
        this.documentHistoryService = documentHistoryService;
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
                relative.getRole().getName().equals("ROLE_MANAGER") == false &&
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
    public DocumentSummary create(DocumentCreateForm documentCreateForm, boolean selectAll,
                                  List<Integer> departmentList, boolean selectAllManager) {
        Integer projectId = documentCreateForm.getProject().getId();

        if (projectRepository.existsById(projectId) == false) {
            throw new EntityNotFoundException(projectId, "Project");
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);

        List<UserEntity> relatives = new ArrayList<>();
        if (selectAll == false) {
            if (documentCreateForm.getRelatives() != null) {
                if (documentCreateForm.getRelatives().isEmpty() && departmentList.isEmpty()) {
                    if (selectAllManager) {
                        List<UserSummary> managerList = userRepository.findAllSummaryByRole_Name("ROLE_MANAGER");
                        for (UserSummary manager : managerList) {
                            UserEntity relative = new UserEntity(manager.getId());
                            relatives.add(relative);
                        }
                    } else {
                        relatives = userRepository.findAll();
                    }
                } else if (!documentCreateForm.getRelatives().isEmpty() && departmentList.isEmpty()) {
                    if (selectAllManager) {
                        for (IdOnlyForm idOnlyForm : documentCreateForm.getRelatives()) {
                            if(userRepository.existsByIdAndRole_Id(idOnlyForm.getId(), 2).booleanValue()){
                                UserEntity relative = new UserEntity(idOnlyForm.getId());
                                relatives.add(relative);
                            }
                        }
                    } else {
                        for (IdOnlyForm idOnlyForm : documentCreateForm.getRelatives()) {
                            UserEntity relative = new UserEntity(idOnlyForm.getId());
                            relatives.add(relative);
                        }
                    }
                } else if (documentCreateForm.getRelatives().isEmpty() && !departmentList.isEmpty()) {
                    if(selectAllManager){
                        for (Integer departmentId : departmentList) {
                            List<UserSummary> userResult = userRepository.findAllSummaryByDepartment_IdAndRole_Id(departmentId, 2);
                            for (UserSummary userSummary : userResult) {
                                UserEntity relative = new UserEntity(userSummary.getId());
                                relatives.add(relative);
                            }
                        }
                    } else {
                        for (Integer departmentId : departmentList) {
                            List<UserSummary> userResult = userRepository.findAllSummaryByDepartment_IdAndRole_IdNotLike(departmentId, 3);
                            for (UserSummary userSummary : userResult) {
                                UserEntity relative = new UserEntity(userSummary.getId());
                                relatives.add(relative);
                            }
                        }
                    }
                } else {
                    if(selectAllManager){
                        for (IdOnlyForm idOnlyForm : documentCreateForm.getRelatives()) {
                            UserEntity relative = userRepository.findById(idOnlyForm.getId()).orElseThrow(
                                    () -> new UserNotFoundException(idOnlyForm.getId())
                            );
                            if (departmentList.contains(relative.getDepartment().getId())
                            && relative.getRole().getId() == 2) {
                                relatives.add(relative);
                            }
                        }
                    } else {
                        for (IdOnlyForm idOnlyForm : documentCreateForm.getRelatives()) {
                            UserEntity relative = userRepository.findById(idOnlyForm.getId()).orElseThrow(
                                    () -> new UserNotFoundException(idOnlyForm.getId())
                            );
                            if (departmentList.contains(relative.getDepartment().getId())) {
                                relatives.add(relative);
                            }
                        }
                    }
                }
            }
        } else {
            relatives = userRepository.findAll();
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

        documentHistoryService.save(savedDocumentEntity);

        DocumentSummary documentSummary = documentRepository.findSummaryById(savedDocumentEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentEntity.getId(), "Document")
        );

        return documentSummary;
    }

    @Override
    public DocumentSummary update(Integer id, DocumentUpdateForm documentUpdateForm, boolean selectAll,
                                  List<Integer> departmentList, boolean selectAllManager) {

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
        if (selectAll == false) {
            if (documentUpdateForm.getRelatives() != null) {
                if (documentUpdateForm.getRelatives().isEmpty() && departmentList.isEmpty()) {
                    if (selectAllManager) {
                        List<UserSummary> managerList = userRepository.findAllSummaryByRole_Name("ROLE_MANAGER");
                        for (UserSummary manager : managerList) {
                            UserEntity relative = new UserEntity(manager.getId());
                            relatives.add(relative);
                        }
                    } else {
                        relatives = userRepository.findAll();
                    }
                } else if (!documentUpdateForm.getRelatives().isEmpty() && departmentList.isEmpty()) {
                    if (selectAllManager) {
                        for (IdOnlyForm idOnlyForm : documentUpdateForm.getRelatives()) {
                            if(userRepository.existsByIdAndRole_Id(idOnlyForm.getId(), 2).booleanValue()){
                                UserEntity relative = new UserEntity(idOnlyForm.getId());
                                relatives.add(relative);
                            }
                        }
                    } else {
                        for (IdOnlyForm idOnlyForm : documentUpdateForm.getRelatives()) {
                            UserEntity relative = new UserEntity(idOnlyForm.getId());
                            relatives.add(relative);
                        }
                    }
                } else if (documentUpdateForm.getRelatives().isEmpty() && !departmentList.isEmpty()) {
                    if(selectAllManager){
                        for (Integer departmentId : departmentList) {
                            List<UserSummary> userResult = userRepository.findAllSummaryByDepartment_IdAndRole_Id(departmentId, 2);
                            for (UserSummary userSummary : userResult) {
                                UserEntity relative = new UserEntity(userSummary.getId());
                                relatives.add(relative);
                            }
                        }
                    } else {
                        for (Integer departmentId : departmentList) {
                            List<UserSummary> userResult = userRepository.findAllSummaryByDepartment_IdAndRole_IdNotLike(departmentId, 3);
                            for (UserSummary userSummary : userResult) {
                                UserEntity relative = new UserEntity(userSummary.getId());
                                relatives.add(relative);
                            }
                        }
                    }
                } else {
                    if(selectAllManager){
                        for (IdOnlyForm idOnlyForm : documentUpdateForm.getRelatives()) {
                            UserEntity relative = userRepository.findById(idOnlyForm.getId()).orElseThrow(
                                    () -> new UserNotFoundException(idOnlyForm.getId())
                            );
                            if (departmentList.contains(relative.getDepartment().getId())
                                    && relative.getRole().getId() == 2) {
                                relatives.add(relative);
                            }
                        }
                    } else {
                        for (IdOnlyForm idOnlyForm : documentUpdateForm.getRelatives()) {
                            UserEntity relative = userRepository.findById(idOnlyForm.getId()).orElseThrow(
                                    () -> new UserNotFoundException(idOnlyForm.getId())
                            );
                            if (departmentList.contains(relative.getDepartment().getId())) {
                                relatives.add(relative);
                            }
                        }
                    }
                }
            }
        } else {
            relatives = userRepository.findAll();
        }

        documentEntity.setProject(projectEntity);
        documentEntity.setTitle(documentUpdateForm.getTitle());
        documentEntity.setSummary(documentUpdateForm.getSummary());
        documentEntity.setDescription(documentUpdateForm.getDescription());
        documentEntity.setStartTime(documentUpdateForm.getStartTime());
        documentEntity.setEndTime(documentUpdateForm.getEndTime());
        documentEntity.setRelatives(relatives);

        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);

        documentHistoryService.save(savedDocumentEntity);

        DocumentSummary documentSummary = documentRepository.findSummaryById(savedDocumentEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentEntity.getId(), "Document")
        );

        return documentSummary;
    }

    @Override
    public Document deleteById(Integer id) {
        Optional<DocumentEntity> documentEntity = documentRepository.findById(id);
        if (documentEntity.isPresent()) {
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
    public Page<DocumentSummary> findAllSummaryByTitleAndSummary(DocumentSearchForm documentSearchForm, Pageable pageable) {
        documentSearchForm.setTitle(documentSearchForm.getTitle().toLowerCase());
        documentSearchForm.setSummary(documentSearchForm.getSummary().toLowerCase());

        // get current logged user
        String email = authenticationFacade.getAuthentication().getName();
        UserEntity creator = userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        if (documentSearchForm.getCreatedTimeFrom() != null && documentSearchForm.getCreatedTimeTo() != null
                && documentSearchForm.getCreatedTimeFrom().isAfter(documentSearchForm.getCreatedTimeTo())) {
            throw new BadRequestException("createdTimeFrom is after createdTimeTo");
        }

        if (documentSearchForm.getStartTimeFrom() != null && documentSearchForm.getStartTimeTo() != null
                && documentSearchForm.getStartTimeFrom().isAfter(documentSearchForm.getStartTimeTo())) {
            throw new BadRequestException("startTimeFrom is after startTimeTo");
        }

        if (documentSearchForm.getEndTimeFrom() != null && documentSearchForm.getEndTimeTo() != null
                && documentSearchForm.getEndTimeFrom().isAfter(documentSearchForm.getEndTimeTo())) {
            throw new BadRequestException("endTimeFrom is after endTimeTo");
        }

        return documentRepository.advanceSearch(documentSearchForm.getTitle(), documentSearchForm.getSummary(),
                documentSearchForm.getCreatedTimeFrom(), documentSearchForm.getCreatedTimeTo(),
                documentSearchForm.getStartTimeFrom(), documentSearchForm.getStartTimeTo(),
                documentSearchForm.getEndTimeFrom(), documentSearchForm.getEndTimeTo(),
                documentSearchForm.getProjectId(),
                pageable);

    }

}
