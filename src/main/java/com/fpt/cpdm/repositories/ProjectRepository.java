package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.models.projects.ProjectDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {

    List<ProjectDTO> findAllDTOBy();
}
