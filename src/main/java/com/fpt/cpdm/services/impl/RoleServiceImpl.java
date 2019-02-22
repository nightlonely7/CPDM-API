package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.RoleEntity;
import com.fpt.cpdm.exceptions.roles.RoleNameNotFoundException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.repositories.RoleRepository;
import com.fpt.cpdm.services.RoleService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private static final String ROLE_PREFIX = "ROLE_";

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role save(Role model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Role> saveAll(List<Role> models) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Role findById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean existsById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Role> findAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Role> findAllById(List<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Role model) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll(List<Role> models) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Role findByName(String name) {

        RoleEntity roleEntity = roleRepository.findByName(ROLE_PREFIX + name).orElseThrow(
                () -> new RoleNameNotFoundException(name)
        );
        Role role = ModelConverter.roleEntityToModel(roleEntity);

        return role;
    }
}
