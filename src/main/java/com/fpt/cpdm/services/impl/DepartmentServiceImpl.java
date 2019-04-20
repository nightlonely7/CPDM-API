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

import java.util.List;
import java.util.Optional;

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

        if(departmentRepository.existsByNameAndAvailableTrue(department.getName())){
            System.out.println("Department name already existed!");
            return null;
        } else if (departmentRepository.existsByAliasAndAvailableTrue(department.getName())){
            System.out.println("Department alias already existed!");
            return null;
        }

        DepartmentEntity departmentEntity = ModelConverter.departmentModelToEntity(department);
        departmentEntity.setAvailable(true);
        System.out.println(departmentEntity);
        DepartmentEntity savedDepartmentEntity = departmentRepository.save(departmentEntity);
        Department savedDepartment = ModelConverter.departmentEntityToModel(savedDepartmentEntity);

        return savedDepartment;
    }

    @Override
    public Page<DepartmentDTO> findAll(Pageable pageable) {
        return departmentRepository.findAllDTOByAvailableTrue(pageable);
    }

    @Override
    public Page<DepartmentDTO> findByName(String name, Pageable pageable) {
        Page<DepartmentDTO> departments = departmentRepository
                .findAllDTOByNameContainingAndAvailableTrue(name, pageable);
        return departments;
    }

    @Override
    public Page<DepartmentDTO> findByNameAndAlias(String name, String alias, Pageable pageable) {
        Page<DepartmentDTO> departments = departmentRepository
                .findAllDTOByNameContainingAndAliasContainingAndAvailableTrue(name, alias, pageable);
        return departments;
    }

    @Override
    public List<DepartmentDTO> findAll() {
        return departmentRepository.findAllDTOBy();
    }

    @Override
    public DepartmentDTO findDTOById(Integer id) {
        Optional<DepartmentDTO> departmentDTO = departmentRepository.findDTOById(id);
        return departmentDTO.get();
    }

    @Override
    public Department deleteById(Integer id) {
        Optional<DepartmentEntity> departmentEntity = departmentRepository.findById(id);
        if(departmentEntity.isPresent()){
            departmentEntity.get().setAvailable(false);
            departmentRepository.save(departmentEntity.get());
        }
        Department savedDepartment = ModelConverter.departmentEntityToModel(departmentEntity.get());
        return savedDepartment;
    }

    @Override
    public boolean existsByName(String name) {
        return departmentRepository.existsByNameAndAvailableTrue(name);
    }

    @Override
    public boolean existsByAlias(String alias) {
        return departmentRepository.existsByAliasAndAvailableTrue(alias);
    }

}
