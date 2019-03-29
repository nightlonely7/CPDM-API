package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.ProjectEntity;
import com.fpt.cpdm.models.projects.Project;
import com.fpt.cpdm.models.projects.ProjectDTO;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.services.ProjectService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<ProjectDTO> findAllDTO() {
        return projectRepository.findAllDTOBy();
    }

    @Override
    public ProjectDTO findDTOById(Integer id) {
        return projectRepository.findDTOById(id).get();
    }

    @Override
    public Page<ProjectDTO> findAllDTOBy(Pageable pageable) {
        return projectRepository.findAllDTOBy(pageable);
    }

    @Override
    public Page<ProjectDTO> findAllDTOByName(String name, Pageable pageable) {
        Page<ProjectDTO> projectDTOS = projectRepository
                .findAllDTOByNameContaining(name, pageable);
        return projectDTOS;
    }

    @Override
    public Page<ProjectDTO> findAllDTOByNameAndAlias(String name, String alias, Pageable pageable) {
        Page<ProjectDTO> projectDTOS = projectRepository
                .findAllDTOByNameContainingAndAliasContaining(name, alias, pageable);
        return projectDTOS;
    }

    @Override
    public Project save(Project project) {
        ProjectEntity projectEntity = ModelConverter.projectModelToEntity(project);
        ProjectEntity savedProjectEntity = projectRepository.save(projectEntity);
        Project savedProject = ModelConverter.projectEntityToModel(savedProjectEntity);
        return savedProject;
    }

}
