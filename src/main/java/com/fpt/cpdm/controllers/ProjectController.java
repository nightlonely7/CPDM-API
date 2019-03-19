package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.projects.ProjectDTO;
import com.fpt.cpdm.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> findAll() {

        List<ProjectDTO> projects = projectService.findAllDTO();

        return ResponseEntity.ok(projects);
    }
}
