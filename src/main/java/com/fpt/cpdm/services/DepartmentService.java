package com.fpt.cpdm.services;

import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.departments.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Department save(Department department);

    Page<DepartmentDTO> findAll(Pageable pageable);

    Page<DepartmentDTO> findByName(String name, Pageable pageable);

    Page<DepartmentDTO> findByNameAndAlias(String name, String alias, Pageable pageable);

    List<DepartmentDTO> findAll();

    DepartmentDTO findDTOById(Integer id);

    Department deleteById(Integer id);

    boolean existsByName(String name);
}
