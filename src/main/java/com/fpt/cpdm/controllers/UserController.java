package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotValidException;
import com.fpt.cpdm.models.User;
import com.fpt.cpdm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    public ResponseEntity<User> readById(@PathVariable(name = "id") Long id) {

        User user = userService.findById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user,
                                       BindingResult result) {

        return save(null, user, result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable(name = "id") Long id,
                                       @Valid @RequestBody User user,
                                       BindingResult result) {

        return save(id, user, result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id) {

        if (userService.existsById(id) == false) {
            throw new UserNotFoundException("User with id " + id + " is not found!");
        }
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @SuppressWarnings("Duplicates")
    private ResponseEntity<User> save(Long id, User user, BindingResult result) {

        if (result.hasErrors()) {
            List<FieldError> fieldsErrors = result.getFieldErrors();
            StringBuilder stringBuilder = new StringBuilder();
            for (FieldError fieldError : fieldsErrors) {
                stringBuilder
                        .append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append(" \n ");
            }
            throw new UserNotValidException(stringBuilder.toString(), null, true, false);
        }
        user.setId(id);
        User savedUser = userService.save(user);

        return ResponseEntity.ok(savedUser);
    }

}
