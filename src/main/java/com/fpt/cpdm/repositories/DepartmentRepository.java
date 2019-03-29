package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.models.departments.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Integer> {

    Page<DepartmentDTO> findAllDTOByIsAvailableTrue(Pageable pageable);

    Page<DepartmentDTO> findAllDTOByNameContainingAndIsAvailableTrue(String name, Pageable pageable);

    Page<DepartmentDTO> findAllDTOByNameContainingAndAliasContainingAndIsAvailableTrue(String name, String alias, Pageable pageable);

    boolean existsByNameAndIsAvailableTrue(String name);
    
    Optional<DepartmentDTO> findDTOById(Integer id);

    List<DepartmentDTO> findAllDTOBy();

    Optional<DepartmentEntity> findById(Integer id);
}
