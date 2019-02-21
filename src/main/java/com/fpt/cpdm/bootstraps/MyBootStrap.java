package com.fpt.cpdm.bootstraps;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.DepartmentService;
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
    private final DepartmentService departmentService;
    private final Validator validator;

    @Autowired
    public MyBootStrap(UserService userService, DepartmentService departmentService, @Qualifier("defaultValidator") Validator validator) {
        this.userService = userService;
        this.departmentService = departmentService;
        this.validator = validator;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("ApplicationReadyEvent");

        Role role = new Role();
        role.setId(2);

        Department department = new Department();
        department.setName("New Department");
        department.setAlias("NEW");
        Department savedDepartment = departmentService.save(department);

        User user = new User();
        user.setId(null);
        user.setDisplayName("Hoàng Vinh Quang");
        user.setEmail("quanghvse61073@fpt.edu.vn");
        user.setPassword("12345678");

        user.setPhone("0707518178");
        user.setAddress("HCMC");
        user.setBirthday(LocalDate.of(1993, Month.SEPTEMBER, 30));
        user.setDepartment(savedDepartment);
        user.setRole(role);
        System.out.println(user);

        User user2 = new User();
        user2.setId(null);
        user2.setDisplayName("Nguyễn Huỳnh Bách Nhân");
        user2.setEmail("nhannhbse62643@fpt.edu.vn");
        user2.setPassword("12345678");
        user2.setPhone("0912345678");
        user2.setAddress("HCMC");
        user2.setBirthday(LocalDate.of(1992, Month.OCTOBER, 9));
        user2.setDepartment(savedDepartment);
        user2.setRole(role);
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
