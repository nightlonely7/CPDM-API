package com.fpt.cpdm.validators.users;

import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleMustExistValidator implements ConstraintValidator<RoleMustExist, Role> {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleMustExistValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void initialize(RoleMustExist constraintAnnotation) {

    }

    @Override
    public boolean isValid(Role role, ConstraintValidatorContext context) {
        return roleRepository.existsById(role.getId());
    }
}
