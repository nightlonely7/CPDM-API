package com.fpt.cpdm.services;

import com.fpt.cpdm.models.projects.Project;
import com.fpt.cpdm.models.projects.ProjectDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    List<ProjectDTO> findAllDTO();

    ProjectDTO findDTOById(Integer id);

    Page<ProjectDTO> findAllDTOBy(Pageable pageable);

    Page<ProjectDTO> findAllDTOByName(String name, Pageable pageable);

    boolean existsByName(String name);

    Page<ProjectDTO> findAllDTOByNameAndAlias(String name, String alias, Pageable pageable);

    Project save(Project project);

}
