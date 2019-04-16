package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.projects.Project;
import com.fpt.cpdm.models.projects.ProjectDTO;
import com.fpt.cpdm.services.ProjectService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<ProjectDTO>> findAll() {

        List<ProjectDTO> projects = projectService.findAllDTO();

        return ResponseEntity.ok(projects);
    }

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> findAll(@PageableDefault Pageable pageable) {

        Page<ProjectDTO> projects = projectService.findAllDTOBy(pageable);

        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> findById(@PathVariable("id") Integer id) {
        ProjectDTO departmentDTO = projectService.findDTOById(id);
        return ResponseEntity.ok(departmentDTO);
    }

    @PostMapping
    public ResponseEntity<Project> create(@Valid @RequestBody Project project,
                                             BindingResult result) {
        return save(null, project, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(@PathVariable("id") Integer id,
                                             @Valid @RequestBody Project project,
                                             BindingResult result) {
        return save(id, project, result);
    }

    public ResponseEntity<Project> save(Integer id, Project project,
                                           BindingResult result){
        if(result.hasErrors()){
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        project.setId(id);
        Project projectResult = projectService.save(project);

        return ResponseEntity.ok(projectResult);
    }

    @GetMapping("/search/name")
    public ResponseEntity<Page<ProjectDTO>> findByName(@RequestParam("name") String name, Pageable pageable) {
        Page<ProjectDTO> projectDTOS = projectService.findAllDTOByName(name, pageable);
        if (projectDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(projectDTOS);
    }

    @GetMapping("/search/nameAndAlias")
    public ResponseEntity<Page<ProjectDTO>> findByNameAndAlias(@RequestParam("name") String name,
                                                                  @RequestParam("alias") String alias,
                                                                  Pageable pageable) {
        Page<ProjectDTO> projectDTOS = projectService.findAllDTOByNameAndAlias(name, alias, pageable);
        if (projectDTOS.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(projectDTOS);
    }

    @GetMapping("/check/existByName")
    public ResponseEntity<Boolean> existsByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(projectService.existsByName(name));
    }

    @GetMapping("/check/existByAlias")
    public ResponseEntity<Boolean> existsByAlias(@RequestParam("alias") String alias) {
        return ResponseEntity.ok(projectService.existsByName(alias));
    }

}
