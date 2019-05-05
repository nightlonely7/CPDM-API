package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.DocumentFileEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.forms.documents.files.DocumentFileCreateForm;
import com.fpt.cpdm.forms.documents.files.DocumentFileUpdateForm;
import com.fpt.cpdm.models.documents.document_files.DocumentFileDetail;
import com.fpt.cpdm.repositories.DocumentFilesRepository;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.DocumentFileService;
import com.fpt.cpdm.services.FileStorageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DocumentFileServiceImpl implements DocumentFileService {

    private final DocumentFilesRepository documentFilesRepository;
    private final DocumentRepository documentRepository;
    private final AuthenticationService authenticationService;
    private final FileStorageService fileStorageService;

    public DocumentFileServiceImpl(DocumentFilesRepository documentFilesRepository, DocumentRepository documentRepository, AuthenticationService authenticationService, FileStorageService fileStorageService) {
        this.documentFilesRepository = documentFilesRepository;
        this.documentRepository = documentRepository;
        this.authenticationService = authenticationService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public DocumentFileDetail create(Integer id, DocumentFileCreateForm documentFileCreateForm) {
        DocumentEntity documentEntity = documentRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "Document")
        );
        UserEntity currentLoggedUser = authenticationService.getCurrentLoggedUser();

        LocalDateTime now = LocalDateTime.now();
        String fileName = documentFileCreateForm.getFilename();
        String originalFilename = documentFileCreateForm.getFile().getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String detailFilename = documentEntity.getProject().getAlias() + "_" +
                documentEntity.getTitle() + "_" +
                currentLoggedUser.getEmail() + "_" +
                now.toString().substring(0, now.toString().indexOf("."))
                        .replaceFirst("T", " ")
                        .replaceFirst(":", "h")
                        .replaceFirst(":", "m")
                + "_" + fileName + "." + extension;

        fileStorageService.store(documentFileCreateForm.getFile(), detailFilename);
        DocumentFileEntity documentFileEntity = new DocumentFileEntity();
        documentFileEntity.setDocument(documentEntity);
        documentFileEntity.setCreator(currentLoggedUser);
        documentFileEntity.setCreatedTime(now);
        documentFileEntity.setLastEditor(currentLoggedUser);
        documentFileEntity.setLastModifiedTime(now);
        documentFileEntity.setFilename(fileName);
        documentFileEntity.setDetailFilename(detailFilename);
        documentFileEntity.setExtension(extension);
        documentFileEntity.setDescription(documentFileCreateForm.getDescription());

        DocumentFileEntity savedDocumentFileEntity = documentFilesRepository.save(documentFileEntity);

        DocumentFileDetail savedDocumentFileDetail = documentFilesRepository.findDetailByIdAndAvailableTrue(savedDocumentFileEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentFileEntity.getId(), "DocumentFile")
        );

        return null;
    }

    @Override
    public DocumentFileDetail update(Integer id, DocumentFileUpdateForm documentFileUpdateForm) {
        DocumentFileEntity documentFileEntity = documentFilesRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "DocumentFile")
        );
        UserEntity currentLoggedUser = authenticationService.getCurrentLoggedUser();

        LocalDateTime now = LocalDateTime.now();
        String fileName= documentFileUpdateForm.getFilename();

        if(documentFileUpdateForm.getFile() != null){
            String originalFileName = documentFileUpdateForm.getFile().getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf(".") +1);

            String detailFileName = documentFileEntity.getDocument().getProject().getAlias() + "_" +
                    documentFileEntity.getDocument().getTitle() + "_" +
                    currentLoggedUser.getEmail() + "_" +
                    now.toString().substring(0, now.toString().indexOf("."))
                            .replaceFirst("T", " ")
                            .replaceFirst(":", "h")
                            .replaceFirst(":", "m")
                    + "_" + fileName + "." + extension;
            fileStorageService.store(documentFileUpdateForm.getFile(), detailFileName);
            documentFileEntity.setDetailFilename(detailFileName);
            documentFileEntity.setExtension(extension);
        } else {
            String detailFileName = documentFileEntity.getDocument().getProject().getAlias() + "_" +
                    documentFileEntity.getDocument().getTitle() + "_" +
                    currentLoggedUser.getEmail() + "_" +
                    documentFileEntity.getCreatedTime().toString().substring(0,
                            documentFileEntity.getCreatedTime().toString().indexOf("."))
                            .replaceFirst("T", " ")
                            .replaceFirst(":", "h")
                            .replaceFirst(":", "m") + "_" + fileName
                                    + "." + documentFileEntity.getExtension();
            documentFileEntity.setDetailFilename(detailFileName);
        }
        documentFileEntity.setLastEditor(currentLoggedUser);
        documentFileEntity.setLastModifiedTime(now);
        documentFileEntity.setFilename(fileName);

        documentFileEntity.setDescription(documentFileUpdateForm.getDescription());
        DocumentFileEntity savedDocumentFileEntity = documentFilesRepository.save(documentFileEntity);
        DocumentFileDetail savedDocumentFileDetail = documentFilesRepository.findDetailByIdAndAvailableTrue(savedDocumentFileEntity.getId()).orElseThrow(
                () -> new EntityNotFoundException(savedDocumentFileEntity.getId(), "DocumentFile")
        );
        return savedDocumentFileDetail;
    }

    @Override
    public void delete(Integer id) {
        DocumentFileEntity documentFileEntity = documentFilesRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "DocumentFile")
        );
        documentFileEntity.setAvailable(Boolean.FALSE);
        documentFilesRepository.save(documentFileEntity);
    }

    public List<DocumentFileDetail> findAllDetailByDocument_Id(Integer id) {
        return documentFilesRepository.findAllDetailByDocument_IdAndAvailableTrue(id);
    }

}
