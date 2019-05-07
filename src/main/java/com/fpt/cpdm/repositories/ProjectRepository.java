package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.models.projects.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

    List<ProjectDTO> findAllDTOBy();

    Page<ProjectDTO> findAllDTOByAndAvailableTrue(Pageable pageable);

    Page<ProjectDTO> findAllDTOByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByAlias(String alias);

    Page<ProjectDTO> findAllDTOByNameContainingIgnoreCaseAndAliasContainingIgnoreCaseAndAvailableTrue
            (String name, String alias, Pageable pageable);

    Optional<ProjectDTO> findDTOById(Integer id);

}

