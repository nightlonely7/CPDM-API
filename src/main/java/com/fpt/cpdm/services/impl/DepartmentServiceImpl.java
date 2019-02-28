package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.exceptions.departments.DepartmentNotFoundException;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.departments.DepartmentDTO;
import com.fpt.cpdm.repositories.DepartmentRepository;
import com.fpt.cpdm.services.DepartmentService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public Department save(Department department) {

        // check department exists (can be null)
        if (department.getId() != null && departmentRepository.existsById(department.getId()) == false) {
            throw new DepartmentNotFoundException(department.getId());
        }

        DepartmentEntity departmentEntity = ModelConverter.departmentModelToEntity(department);
        DepartmentEntity savedDepartmentEntity = departmentRepository.save(departmentEntity);
        Department savedDepartment = ModelConverter.departmentEntityToModel(savedDepartmentEntity);

        return savedDepartment;
    }

    @Override
    public Page<DepartmentDTO> findAll(Pageable pageable) {
        return departmentRepository.findAllDTOBy(pageable);
    }

}
