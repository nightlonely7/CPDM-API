package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.PolicyForFree;
import com.fpt.cpdm.models.leaveRequests.Leave;
import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.leaveRequests.LeaveRequestSummary;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.models.users.UserLeaves;
import com.fpt.cpdm.models.users.UserSummary;
import com.fpt.cpdm.services.LeaveRequestService;
import com.fpt.cpdm.services.TaskService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ConstantManager;
import com.fpt.cpdm.utils.Enum;
import com.fpt.cpdm.utils.ModelErrorMessage;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonProperties;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static com.fpt.cpdm.utils.ConstantManager.defaultNumberOfDayOffFreeCheck;
import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequestMapping("/leaveRequests")
public class LeaveRequestController {

    private LeaveRequestService leaveRequestService;
    private UserService userService;
    private TaskService taskService;

    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService, UserService userService, TaskService taskService) {
        this.leaveRequestService = leaveRequestService;
        this.userService = userService;
        this.taskService = taskService;
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

        //Get number of day off requested
        int diff = (int) DAYS.between(leaveRequest.getFromDate(),leaveRequest.getToDate());

        ArrayList<PolicyForFree> policyForFrees = new ArrayList<>();

        //Get number of day off free check in json file default 3
        Integer numberOfDateFreeCheck = ConstantManager.defaultNumberOfDayOffFreeCheck;
        try {
            FileReader fr = new FileReader(ConstantManager.policyForLeavePath);
            JSONParser parser = new JSONParser(fr);
            List list = parser.list();
            if(list.size() > 0){
                List<PolicyForFree> policyForFreeList = (List<PolicyForFree>) list;
                policyForFreeList.sort((o1, o2) -> o1.getValidFromDate().compareTo(o2.getValidFromDate()));
                LocalDate fromDate = leaveRequest.getFromDate();
                for ( PolicyForFree item :policyForFreeList) {
                    if(fromDate.isAfter(item.getValidFromDate())){
                        if(item.getNumberOfDayOffFreeCheck() != null){
                            numberOfDateFreeCheck = item.getNumberOfDayOffFreeCheck();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        //Check number of day of policy rule
        User user = userService.findByEmail(principal.getName());
        //Deny if exist working task if request days greater than policy free check days
        if(diff > numberOfDateFreeCheck){
            if(taskService.existsByExecutorAndStatus(user, "Working")){
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
        }
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
    public ResponseEntity<List<LeaveRequestSummary>> findByUserAndDateRange(
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

    @GetMapping("/viewLeaves")
    public ResponseEntity<Page<UserLeaves>> viewLeaves(
            @RequestParam String fromDate, @RequestParam String toDate,
            @PageableDefault Pageable pageable) {

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        Page<UserSummary> userSummaries = userService.findAllSummaryForAdmin(pageable);

        ArrayList<UserLeaves> userLeaveList = new ArrayList<UserLeaves>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();

        int countPlusDate = -1;
        for ( UserSummary userSummary : userSummaries) {
            UserLeaves userLeaves = new UserLeaves();
            userLeaves.setDisplayName(userSummary.getDisplayName());
            ArrayList<Leave> list = new ArrayList<>();
            User user = new User();
            user.setId(userSummary.getId());
            while(from.plusDays(countPlusDate).isBefore(to)){
                LocalDate tmpDate = from.plusDays(countPlusDate);
                countPlusDate++;
                Leave leave = new Leave();
                leave.setDate(tmpDate);
                leave.setWaiting(false);
                leave.setApproved(false);
                if(leaveRequestService.existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(tmpDate,tmpDate,user,approvedCode)){
                    leave.setApproved(true);
                } else if(leaveRequestService.existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(tmpDate,tmpDate,user,newCode)){
                    leave.setWaiting(true);
                }
                list.add(leave);
            }
            countPlusDate = -1;
            userLeaves.setLeaveList(list);
            userLeaveList.add(userLeaves);
        }

        Page<UserLeaves> PageImpl = new PageImpl<UserLeaves>(userLeaveList, new PageRequest(pageable.getPageNumber(),pageable.getPageSize(),pageable.getSort()),userLeaveList.size());

        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(PageImpl);
    }

}
