package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.AssignRequestService;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/assignRequests")
public class AssignRequestController {
    private AssignRequestService assignRequestService;
    private UserService userService;
    private TaskService taskService;

    @Autowired
    public AssignRequestController(AssignRequestService assignRequestService, UserService userService, TaskService taskService) {
        this.assignRequestService = assignRequestService;
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/search/findByUser")
    public ResponseEntity<Page<AssignRequestSummary>> findbyUser(@RequestParam Integer status,
                                                                 @PageableDefault Pageable pageable,
                                                                 Principal principal) {
        // get current logged user
        User user = userService.findByEmail(principal.getName());

        Page<AssignRequestSummary> assignRequestSummaries = assignRequestService.findAllSummaryByUserAndStatus(user, status, pageable);
        if (assignRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(assignRequestSummaries);
    }

    @GetMapping("/search/findByApprover")
    public ResponseEntity<Page<AssignRequestSummary>> findByApprover(@RequestParam Integer status,
                                                                     @PageableDefault Pageable pageable,
                                                                     Principal principal) {
        // get current logged user
        User approver = userService.findByEmail(principal.getName());

        Page<AssignRequestSummary> assignRequestSummaries = assignRequestService.findAllSummaryByApproverAndStatus(approver, status, pageable);
        if (assignRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(assignRequestSummaries);
    }

    @GetMapping("/search/findByTaskAndDateRange/{taskId}")
    public ResponseEntity<Page<AssignRequestSummary>> findByTaskAndDateRange(@PathVariable(name = "taskId") Integer taskId,
                                                                             @RequestParam String fromDate, @RequestParam String toDate,
                                                                             @PageableDefault Pageable pageable,
                                                                             Principal principal) {
        //Parse from to date
        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        // get current logged user
        User user = userService.findByEmail(principal.getName());
        // get status list waiting or approved
        ArrayList<Integer> statusList = new ArrayList<>();
        statusList.add(Enum.AssignRequestStatus.New.getAssignRequestStatusCode());
        statusList.add(Enum.AssignRequestStatus.Approved.getAssignRequestStatusCode());

        List<AssignRequestSummary> assignRequestSummaries = assignRequestService.findAllSummaryByTaskAndDateRange(user, taskId, from, to, statusList);
        if (assignRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        Page<AssignRequestSummary> result = new PageImpl<AssignRequestSummary>(assignRequestSummaries,new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()),assignRequestSummaries.size());
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<AssignRequest> create(@Valid @RequestBody AssignRequest assignRequest,
                                                BindingResult result, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        assignRequest.setUser(user);

        return save(null, assignRequest, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssignRequest> edit(@PathVariable(name = "id") Integer id,
                                              @Valid @RequestBody AssignRequest assignRequest, BindingResult result, Principal principal) {

        if (assignRequestService.findById(id).getStatus() == Enum.AssignRequestStatus.Approved.getAssignRequestStatusCode()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }

        return save(id, assignRequest, result, principal);
    }

    private ResponseEntity<AssignRequest> save(Integer id, AssignRequest assignRequest, BindingResult result, Principal principal) {

        LocalDate today = LocalDate.now();
        //Validate date from date must after today
        if (assignRequest.getFromDate().isBefore(today)) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        //validate to date >= from date
        if (assignRequest.getToDate().isBefore(assignRequest.getFromDate())) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        assignRequest.setId(id);

        AssignRequest savedAssignRequest = assignRequestService.save(assignRequest);

        return ResponseEntity.ok(savedAssignRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        if (assignRequestService.findById(id).getStatus() == Enum.AssignRequestStatus.Approved.getAssignRequestStatusCode()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        assignRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
