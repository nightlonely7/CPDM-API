package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserSummary;
import com.fpt.cpdm.services.LeaveRequestService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/leaveRequests")
public class LeaveRequestController {

    private LeaveRequestService leaveRequestService;
    private UserService userService;

    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService, UserService userService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
    }

    @GetMapping("/findByUser")
    public ResponseEntity<Page<LeaveRequestSummary>> findbyUser(@RequestParam Integer status,
                                                                @PageableDefault Pageable pageable,
                                                                Principal principal) {
        // get current logged user
        User user = userService.findByEmail(principal.getName());

        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestService.findAllSummaryByUserAndStatus(user, status, pageable);
        if (leaveRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequestSummaries);
    }

    @GetMapping("/findByApprover")
    public ResponseEntity<Page<LeaveRequestSummary>> findByApprover(@RequestParam Integer status,
                                                                    @PageableDefault Pageable pageable,
                                                                    Principal principal) {
        // get current logged user
        User approver = userService.findByEmail(principal.getName());

        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestService.findAllSummaryByApproverAndStatus(approver, status, pageable);
        if (leaveRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequestSummaries);
    }

    @PostMapping
    public ResponseEntity<LeaveRequest> create(@Valid @RequestBody LeaveRequest leaveRequest,
                                               BindingResult result, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        leaveRequest.setUser(user);

        return save(null, leaveRequest, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeaveRequest> edit(@PathVariable(name = "id") Integer id,
                                             @Valid @RequestBody LeaveRequest leaveRequest, BindingResult result, Principal principal) {
        if (leaveRequestService.findById(id).getStatus() == Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }

        return save(id, leaveRequest, result, principal);
    }

    private ResponseEntity<LeaveRequest> save(Integer id, LeaveRequest leaveRequest, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        leaveRequest.setId(id);

        LeaveRequest savedLeaveRequest = leaveRequestService.save(leaveRequest);

        return ResponseEntity.ok(savedLeaveRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        if (leaveRequestService.findById(id).getStatus() == Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        leaveRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findByUserAndDateRange")
    public ResponseEntity<List<LeaveRequestSummary>> viewLeaves(
            @RequestParam String fromDate, @RequestParam String toDate, @RequestParam Integer userId) {

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        User user = new User();
        user.setId(userId);

        List<LeaveRequestSummary> leaveRequestSummaries =  leaveRequestService.findAllSummaryByFromDateGreaterThanEqualOrToDateLessThanEqualAndUser(to,from,user);


        if (leaveRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequestSummaries);
    }

}
