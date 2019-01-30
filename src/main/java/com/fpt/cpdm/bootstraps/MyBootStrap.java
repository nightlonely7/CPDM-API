package com.fpt.cpdm.bootstraps;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.User;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;

import java.time.LocalDate;
import java.time.Month;

@Slf4j
@Component
public class MyBootStrap implements ApplicationListener<ApplicationReadyEvent> {

    private final UserService userService;
    private final Validator validator;

    @Autowired
    public MyBootStrap(UserService userService, @Qualifier("defaultValidator") Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("ApplicationReadyEvent");
        User user = new User();
        user.setId(null);
        user.setDisplayName("Hoàng Vinh Quang");
        user.setEmail("quanghvse61073@fpt.edu.vn");
        user.setPassword("12345678");
        Role role = new Role();
        role.setId(1);
        user.setRole(role);
        user.setPhone("0707518178");
        user.setAddress("HCMC");
        user.setBirthday(LocalDate.of(1993, Month.SEPTEMBER, 30));
        System.out.println(user);

        BindingResult result = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, result);

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        User savedUser = userService.save(user);
        System.out.println(savedUser);

        UserDetails currentUser = userService.loadUserByUsername("quanghvse61073@fpt.edu.vn");
        System.out.println(currentUser);

    }
}
