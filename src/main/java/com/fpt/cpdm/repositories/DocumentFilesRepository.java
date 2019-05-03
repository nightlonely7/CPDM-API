package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentFileEntity;
import com.fpt.cpdm.models.documents.document_files.DocumentFileDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentFilesRepository extends JpaRepository<DocumentFileEntity, Integer> {

    Optional<DocumentFileDetail> findDetailByIdAndAvailableTrue(Integer id);

    List<DocumentFileDetail> findAllDetailByDocument_IdAndAvailableTrue(Integer id);

    List<DocumentFileDetail> findAllDetailByDocument_Id(Integer id);
}
