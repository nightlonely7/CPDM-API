package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.services.RoleService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<User>> readAll() {

        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> readById(@PathVariable(name = "id") Integer id) {

        User user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @GetMapping("/findAllStaffDisplayNameByDepartmentOfCurrentLoggedManager")
    public ResponseEntity<List<UserDisplayName>> findAllStaffDisplayNameByDepartmentOfCurrentLoggedManager(Principal principal) {

        // get current logged manager
        User user = userService.findByEmail(principal.getName());

        // get role 'STAFF'
        Role role = roleService.findByName("STAFF");

        List<UserDisplayName> userDisplayNames = userService
                .findDisplayNameByDepartmentAndRole(user.getDepartment(), role);
        if (userDisplayNames.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userDisplayNames);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user,
                                       BindingResult result) {

        return save(null, user, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable(name = "id") Integer id,
                                       @Valid @RequestBody User user,
                                       BindingResult result) {

        return save(id, user, result);
    }

    private ResponseEntity<User> save(Integer id, User user, BindingResult result) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        user.setId(id);
        User savedUser = userService.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {

        if (!userService.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
