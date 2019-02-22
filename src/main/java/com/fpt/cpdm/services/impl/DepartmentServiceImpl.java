package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.exceptions.departments.DepartmentNotFoundException;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.repositories.DepartmentRepository;
import com.fpt.cpdm.services.DepartmentService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<Department> saveAll(List<Department> departments) {
        // TODO
        return null;
    }

    @Override
    public Department findById(Integer id) {
        // TODO
        return null;
    }

    @Override
    public boolean existsById(Integer id) {
        return departmentRepository.existsById(id);
    }

    @Override
    public List<Department> findAll() {
        // TODO
        return null;
    }

    @Override
    public List<Department> findAllById(List<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        return departmentRepository.count();
    }

    @Override
    public void deleteById(Integer id) {
        // TODO
    }

    @Override
    public void delete(Department department) {
        // TODO
    }

    @Override
    public void deleteAll(List<Department> departments) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
