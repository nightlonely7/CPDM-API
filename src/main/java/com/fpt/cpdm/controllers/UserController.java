package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserDetail;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.models.users.UserSummary;
import com.fpt.cpdm.services.RoleService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

        List<User> users = null;
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetail> readById(
            @PathVariable(name = "id") Integer id,
            Principal principal) {

        UserDetail userDetail = userService.findDetailById(id, principal);

        return ResponseEntity.ok(userDetail);
    }

    @GetMapping("/findAllStaffDisplayNameByDepartmentOfCurrentLoggedManager")
    public ResponseEntity<List<UserDisplayName>> findAllStaffDisplayNameByDepartmentOfCurrentLoggedManager(Principal principal) {

        // get current logged manager
        User user = userService.findByEmail(principal.getName());

        List<UserDisplayName> userDisplayNames = userService
                .findDisplayNameByDepartmentAndRole_Name(user.getDepartment(), "ROLE_STAFF");
        if (userDisplayNames.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userDisplayNames);
    }

    @GetMapping("/findAllStaffSummaryByDepartmentOfCurrentLoggedManager")
    public ResponseEntity<Page<UserSummary>> findAllStaffSummaryByDepartmentOfCurrentLoggedManager(
            @PageableDefault Pageable pageable,
            Principal principal) {

        // get current logged manager
        User user = userService.findByEmail(principal.getName());

        Page<UserSummary> userSummaries = userService
                .findSummaryByDepartmentAndRole_Name(user.getDepartment(), "ROLE_STAFF", pageable);
        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userSummaries);
    }

    @PostMapping
    public ResponseEntity<UserDetail> create(@Valid @RequestBody User user,
                                             BindingResult result,
                                             Principal principal) {

        return save(null, user, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetail> update(@PathVariable(name = "id") Integer id,
                                             @Valid @RequestBody User user,
                                             BindingResult result,
                                             Principal principal) {

        return save(id, user, result, principal);
    }

    private ResponseEntity<UserDetail> save(Integer id, User user, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        user.setId(id);
        UserDetail savedUserDetail = userService.save(user, principal);

        return ResponseEntity.ok(savedUserDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {


        return ResponseEntity.noContent().build();
    }


}
