package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.departments.DepartmentDTO;
import com.fpt.cpdm.services.DepartmentService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
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

    @GetMapping("/search/name")
    public ResponseEntity<Page<DepartmentDTO>> findByName(@RequestParam("name") String name,
                                                          @PageableDefault Pageable pageable) {
        Page<DepartmentDTO> departmentDTOs = departmentService.findByName(name, pageable);
        if (departmentDTOs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(departmentDTOs);
    }

    @GetMapping("/search/nameAndAlias")
    public ResponseEntity<Page<DepartmentDTO>> findByNameAndAlias(@RequestParam("name") String name,
                                                                  @RequestParam("alias") String alias,
                                                                  @PageableDefault Pageable pageable) {
        Page<DepartmentDTO> departmentDTOs = departmentService.findByNameAndAlias(name, alias, pageable);
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
    public ResponseEntity<DepartmentDTO> findById(@PathVariable("id") Integer id) {
        DepartmentDTO departmentDTO = departmentService.findDTOById(id);
        return ResponseEntity.ok(departmentDTO);
    }

    @PostMapping
    public ResponseEntity<Department> create(@Valid @RequestBody Department department,
                                                BindingResult result) {
        return save(null, department, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable("id") Integer id,
                                 @Valid @RequestBody Department department,
                                                BindingResult result) {
        return save(id, department, result);
    }

    public ResponseEntity<Department> save(Integer id, Department department,
                                              BindingResult result){
        if(result.hasErrors()){
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        department.setId(id);
        Department departmentResult = departmentService.save(department);

        return ResponseEntity.ok(departmentResult);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        departmentService.deleteById(id);
    }

    @GetMapping("/check/existByName")
    public ResponseEntity<Boolean> existByName(@RequestParam("name") String name){
        return ResponseEntity.ok(departmentService.existsByName(name));
    }

    @GetMapping("/check/existByAlias")
    public ResponseEntity<Boolean> existByAlias(@RequestParam("alias") String alias){
        return ResponseEntity.ok(departmentService.existsByAlias(alias));
    }

}
