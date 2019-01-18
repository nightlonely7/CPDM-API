package com.fpt.cpdm.validators.users;

import com.fpt.cpdm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotDuplicateValidator implements ConstraintValidator<EmailNotDuplicate, String> {

    private final UserRepository userRepository;

    @Autowired
    public EmailNotDuplicateValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initialize(EmailNotDuplicate constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !userRepository.existsByEmail(value);
    }
}
