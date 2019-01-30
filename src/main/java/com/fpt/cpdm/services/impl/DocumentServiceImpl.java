package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.exceptions.documents.DocumentNotFoundException;
import com.fpt.cpdm.models.Document;
import com.fpt.cpdm.repositories.DocumentRepository;
import com.fpt.cpdm.services.DocumentService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document save(Document document) {

        // check id exist
        if (document.getId() != null && documentRepository.existsById(document.getId()) == false) {
            throw new DocumentNotFoundException(document.getId());
        }

        DocumentEntity documentEntity = ModelConverter.documentModelToEntity(document);
        DocumentEntity savedDocumentEntity = documentRepository.save(documentEntity);
        Document savedDocument = ModelConverter.documentEntityToModel(savedDocumentEntity);

        return savedDocument;
    }

    @Override
    public List<Document> saveAll(List<Document> entities) {
        // TODO
        return null;
    }

    @Override
    public Document findById(Integer id) {
        // TODO
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        // TODO
        return false;
    }

    @Override
    public List<Document> findAll() {

        List<DocumentEntity> documentEntities = documentRepository.findAll();
        List<Document> documents = new ArrayList<>();
        for (DocumentEntity documentEntity : documentEntities) {
            Document document = ModelConverter.documentEntityToModel(documentEntity);
            documents.add(document);
        }

        return documents;
    }

    @Override
    public List<Document> findAllById(List<Integer> ids) {
        // TODO
        return null;
    }

    @Override
    public long count() {
        // TODO
        return 0;
    }

    @Override
    public void deleteById(Integer id) {
        // TODO
    }

    @Override
    public void delete(Document entity) {
        // TODO
    }

    @Override
    public void deleteAll(List<Document> entities) {
        // TODO
    }

    @Override
    public void deleteAll() {
        // TODO
    }
}
