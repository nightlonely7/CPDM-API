package com.fpt.cpdm.bootstraps;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.users.User;
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
        role.setId(2);
        user.setRole(role);
        user.setPhone("0707518178");
        user.setAddress("HCMC");
        user.setBirthday(LocalDate.of(1993, Month.SEPTEMBER, 30));
        System.out.println(user);

        User user2 = new User();
        user2.setId(null);
        user2.setDisplayName("Nguyễn Huỳnh Bách Nhân");
        user2.setEmail("nhannhbse62643@fpt.edu.vn");
        user2.setPassword("nhan1992");
        Role role2 = new Role();
        role2.setId(2);
        user2.setRole(role);
        user2.setPhone("0912345678");
        user2.setAddress("HCMC");
        user2.setBirthday(LocalDate.of(1992, Month.OCTOBER, 9));
        System.out.println(user2);

        BindingResult result = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, result);

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        User savedUser = userService.save(user);
        System.out.println(savedUser);

        User savedUser2 = userService.save(user2);
        System.out.println(savedUser2);

        UserDetails currentUser = userService.loadUserByUsername("quanghvse61073@fpt.edu.vn");
        System.out.println(currentUser);

    }
}
