package com.fpt.cpdm.services;

import com.fpt.cpdm.models.projects.ProjectDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> findAllDTO();
}
