package com.fpt.cpdm.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.DocumentHistoryEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.EntityNotFoundException;
import com.fpt.cpdm.models.documents.DocumentSummary;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistoryData;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistoryDataResponse;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistoryResponse;
import com.fpt.cpdm.models.documents.document_histories.DocumentHistorySummary;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.repositories.DocumentHistoryRepository;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.AuthenticationService;
import com.fpt.cpdm.services.DocumentHistoryService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DocumentHistoryServiceImpl implements DocumentHistoryService {

    private final DocumentHistoryRepository documentHistoryRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public DocumentHistoryServiceImpl(DocumentHistoryRepository documentHistoryRepository, DocumentRepository documentRepository, UserRepository userRepository, AuthenticationService authenticationService) {
        this.documentHistoryRepository = documentHistoryRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void save(DocumentEntity documentEntity) {

        DocumentHistoryData documentHistoryData = new DocumentHistoryData();
        documentHistoryData.setTitle(documentEntity.getTitle());
        documentHistoryData.setSummary(documentEntity.getSummary());
        documentHistoryData.setDescription(documentEntity.getDescription());
        documentHistoryData.setCreatedTime(documentEntity.getCreatedTime().toString());
        documentHistoryData.setStartTime(documentEntity.getStartTime().toString());
        documentHistoryData.setEndTime(documentEntity.getEndTime().toString());
        documentHistoryData.setCreatorId(authenticationService.getCurrentLoggedUser().getId());
        documentHistoryData.setAvailable(documentEntity.getAvailable());

        String data = "";
        try {
            data = new ObjectMapper().writeValueAsString(documentHistoryData);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        DocumentHistoryEntity documentHistoryEntity = new DocumentHistoryEntity();
        documentHistoryEntity.setDocument(documentEntity);
        documentHistoryEntity.setCreatedTime(documentEntity.getLastModifiedTime());
        documentHistoryEntity.setData(data);
        documentHistoryEntity.setCreator(authenticationService.getCurrentLoggedUser());
        documentHistoryRepository.save(documentHistoryEntity);
    }

    @Override
    public List<DocumentHistorySummary> findAllSummaryByDocument_Id(Integer documentId) {

        List<DocumentHistorySummary> documentHistorySummaries = documentHistoryRepository
                .findAllSummaryByDocument_Id(documentId);

        return documentHistorySummaries;
    }

    @Override
    public DocumentHistoryResponse findById(Integer id) {
        DocumentHistoryEntity documentHistoryEntity = documentHistoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(id, "DocumentHistory")
        );
        DocumentHistoryResponse documentHistoryResponse = new DocumentHistoryResponse();
        documentHistoryResponse.setId(documentHistoryEntity.getId());

        UserDisplayName historyCreator = userRepository
                .findDisplayNameById(documentHistoryEntity.getCreator().getId()).orElseThrow(
                        () -> new EntityNotFoundException(id, "User")
                );

        documentHistoryResponse.setCreator(historyCreator);
        documentHistoryResponse.setCreatedTime(documentHistoryEntity.getCreatedTime());

        DocumentSummary historyDocument = documentRepository.findSummaryById(documentHistoryEntity.getDocument().getId()).orElseThrow(
                () -> new EntityNotFoundException(documentHistoryEntity.getDocument().getId(), "Task")
        );
        documentHistoryResponse.setDocument(historyDocument);

        DocumentHistoryData documentHistoryData;
        try {
            documentHistoryData = new ObjectMapper().readValue(documentHistoryEntity.getData(), DocumentHistoryData.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't read history data in Document History");
        }

        DocumentHistoryDataResponse documentHistoryDataResponse = new DocumentHistoryDataResponse();

        documentHistoryDataResponse.setTitle(documentHistoryData.getTitle());
        documentHistoryDataResponse.setSummary(documentHistoryData.getSummary());
        documentHistoryDataResponse.setDescription(documentHistoryData.getDescription());
        documentHistoryDataResponse.setCreatedTime(
                LocalDateTime.parse(documentHistoryData.getCreatedTime(), DateTimeFormatter.ISO_DATE_TIME));
        documentHistoryDataResponse.setStartTime(
                LocalDateTime.parse(documentHistoryData.getStartTime(), DateTimeFormatter.ISO_DATE_TIME));
        documentHistoryDataResponse.setEndTime(
                LocalDateTime.parse(documentHistoryData.getEndTime(), DateTimeFormatter.ISO_DATE_TIME));

        UserDisplayName creator = userRepository.findDisplayNameById(documentHistoryData.getCreatorId()).orElseThrow(
                () -> new EntityNotFoundException(documentHistoryData.getCreatorId(), "User")
        );
        documentHistoryDataResponse.setCreator(creator);

        documentHistoryDataResponse.setAvailable(documentHistoryData.getAvailable());

        documentHistoryResponse.setData(documentHistoryDataResponse);

        return documentHistoryResponse;
    }

}
