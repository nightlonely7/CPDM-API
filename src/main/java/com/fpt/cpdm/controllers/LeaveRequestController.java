package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.LeaveRequestService;
import com.fpt.cpdm.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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
    public ResponseEntity<Page<LeaveRequestSummary>> findbyUser(@PageableDefault Pageable pageable,
                                                                Principal principal) {
        // get current logged user
        User user = userService.findByEmail(principal.getName());

        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestService.findAllSummaryByUserOrderByCreatedDateDesc(user,pageable);
        if (leaveRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequestSummaries);
    }

    @GetMapping("/findByApprover")
    public ResponseEntity<Page<LeaveRequestSummary>> findByApprover(@PageableDefault Pageable pageable,
                                                                    Principal principal) {
        // get current logged user
        User approver = userService.findByEmail(principal.getName());

        Page<LeaveRequestSummary> leaveRequestSummaries = leaveRequestService.findAllSummaryByApproverOrderByCreatedDateDesc(approver,pageable);
        if (leaveRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequestSummaries);
    }
}
