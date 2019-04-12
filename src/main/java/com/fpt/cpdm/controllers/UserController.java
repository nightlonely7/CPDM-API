package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.users.*;
import com.fpt.cpdm.services.RoleService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GetMapping("/search/all")
    public ResponseEntity<Page<UserSummary>> findAllForAdmin(
            @PageableDefault Pageable pageable
    ) {
        Page<UserSummary> userSummaries = userService.findAllSummaryForAdmin(pageable);
        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/search/relatedByTask_Id")
    public ResponseEntity<List<UserSummary>> findAllRelatedByTask_Id(@RequestParam("id") Integer id) {

        List<UserSummary> userSummaries = userService.findAllSummaryRelatedByTask_Id(id);

        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/search/findAllForSelectByEmailContaining")
    public ResponseEntity<List<UserForSelect>> findByEmailContaining(@RequestParam("email") String email) {

        if (email.trim().isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UserForSelect> userForSelects = userService.findAllForSelectByEmailContains(email);

        if (userForSelects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(userForSelects);
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

    @Secured("ROLE_ADMIN")
    @GetMapping("/search/findAllManagerSummary")
    public ResponseEntity<List<UserSummary>> findAllManagerForSelect() {

        List<UserSummary> managers = userService.findAllManagerForSelect();

        return ResponseEntity.ok(managers);
    }

    @GetMapping("/findAllDisplayNameByDepartmentAndRoleNameOfCurrentLoggedManager")
    public ResponseEntity<List<UserDisplayName>> findAllDisplayNameByDepartmentAndRoleNameOfCurrentLoggedManager(
            @RequestParam("roleName") String roleName, Principal principal) {

        // get current logged manager
        User user = userService.findByEmail(principal.getName());

        List<UserDisplayName> userDisplayNames = userService
                .findDisplayNameByDepartmentAndRole_Name(user.getDepartment(), roleName);
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

    @GetMapping("/search/email")
    public ResponseEntity<Page<UserSummary>> findAllSummaryByEmail(@RequestParam("searchValue") String email,
                                                                   @RequestParam("departmentId") String depId,
                                                                   @RequestParam("gender") String gender,
                                                                   @PageableDefault Pageable pageable){
        Boolean isGender = gender.equals("null") ? null : (gender.equals("true") ? true: false);
        Integer departmentId = Integer.parseInt(depId);
        Page<UserSummary> userSummaries = userService.findAllSummaryByEmail(email, departmentId, isGender, pageable);
        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/search/displayName")
    public ResponseEntity<Page<UserSummary>> findAllSummaryByDisplayName(@RequestParam("searchValue") String displayName,
                                                                         @RequestParam("departmentId") String depId,
                                                                         @RequestParam("gender") String gender,
                                                                         @PageableDefault Pageable pageable){
        Boolean isGender = gender.equals("null") ? null : (gender.equals("true") ? true: false);
        Integer departmentId = Integer.parseInt(depId);
        Page<UserSummary> userSummaries = userService.findAllSummaryByDisplayName(displayName, departmentId, isGender, pageable);
        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/search/fullName")
    public ResponseEntity<Page<UserSummary>> findAllSummaryByFullName(@RequestParam("searchValue") String fullName,
                                                                      @RequestParam("departmentId") String depId,
                                                                      @RequestParam("gender") String gender,
                                                                      @PageableDefault Pageable pageable){
        Boolean isGender = gender.equals("null") ? null : (gender.equals("true") ? true : false);
        Integer departmentId = Integer.parseInt(depId);
        Page<UserSummary> userSummaries = userService.findAllSummaryByFullName(fullName, departmentId, isGender, pageable);
        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/search/age")
    public ResponseEntity<Page<UserSummary>> findAllSummaryByAge(@RequestParam("birthDateFrom") String birthDateFrom,
                                                                      @RequestParam("birthDateTo") String birthDateTo,
                                                                      @RequestParam("gender") String gender,
                                                                      @PageableDefault Pageable pageable){
        LocalDate dateFrom = LocalDate.parse(birthDateFrom);
        LocalDate dateTo = LocalDate.parse(birthDateTo);
        Boolean isGender = gender.equals("null") ? null : (gender.equals("true") ? true : false);
        Page<UserSummary> userSummaries = userService.findAllSummaryByAge(dateFrom, dateTo, isGender, pageable);
        return ResponseEntity.ok(userSummaries);
    }

    @GetMapping("/search/maxMinAge")
    public ResponseEntity<List<UserBirthDate>> findMaxAndMinAge(){
        System.out.println(userService.findMaxAndMinAge());
        return ResponseEntity.ok(userService.findMaxAndMinAge());
    }

    @PostMapping
    public ResponseEntity<UserDetail> create(@Valid @RequestBody User user,
                                             BindingResult result,
                                             Principal principal) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        user.setId(null);
        UserDetail savedUserDetail = userService.create(user, principal);

        return ResponseEntity.ok(savedUserDetail);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDetail> update(@PathVariable(name = "id") Integer id,
                                             @Valid @RequestBody User user,
                                             BindingResult result,
                                             Principal principal) {
        System.out.println(user.toString());
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        user.setId(id);
        System.out.println("Update user: " + user.toString());
        UserDetail savedUserDetail = userService.update(user, principal);

        return ResponseEntity.ok(savedUserDetail);
    }

    @PutMapping("/personalUpdate/{id}")
    public ResponseEntity<UserDetail> personalUpdate(@PathVariable(name = "id") Integer id,
                                             @Valid @RequestBody User user,
                                             BindingResult result,
                                             Principal principal) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        user.setId(id);
        UserDetail savedUserDetail = userService.personalUpdate(user, principal);

        return ResponseEntity.ok(savedUserDetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check/existByEmail")
    public ResponseEntity<Boolean> existsByEmail(@Valid @RequestParam("email") String email){
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

}
