package com.fpt.cpdm.services;

import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.departments.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

    Department save(Department department);

    Page<DepartmentDTO> findAll(Pageable pageable);
}
