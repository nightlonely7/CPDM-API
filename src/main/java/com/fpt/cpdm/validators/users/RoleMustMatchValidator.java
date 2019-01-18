package com.fpt.cpdm.validators.users;

import com.fpt.cpdm.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RoleMustMatchValidator implements ConstraintValidator<RoleMustMatch, String> {

    private static final String ROLE_PREFIX = "ROLE_";

    private final RoleRepository roleRepository;

    @Autowired
    public RoleMustMatchValidator(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void initialize(RoleMustMatch constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String roleName = ROLE_PREFIX + value;
        return roleRepository.existsByName(roleName);
    }
}
