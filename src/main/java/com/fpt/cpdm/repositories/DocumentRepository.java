package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

}
