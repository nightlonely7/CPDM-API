package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.departments.DepartmentDTO;
import com.fpt.cpdm.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public ResponseEntity<Page<DepartmentDTO>> findAll(Pageable pageable) {

        Page<DepartmentDTO> departmentDTOs = departmentService.findAll(pageable);
        if (departmentDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departmentDTOs);
    }

    @GetMapping("/search/all")
    public ResponseEntity<List<DepartmentDTO>> findAllNameOnly() {
        List<DepartmentDTO> departmentDTOs = departmentService.findAll();
        if (departmentDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departmentDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable("id") Integer id) {
        return null;

    }
}
