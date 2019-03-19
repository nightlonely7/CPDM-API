package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.models.projects.ProjectDTO;
import com.fpt.cpdm.repositories.ProjectRepository;
import com.fpt.cpdm.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
