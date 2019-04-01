package com.fpt.cpdm.controllers;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.PolicyForLeave;
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
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/search/findByUser")
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

    @GetMapping("/search/findByApprover")
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
        //validate to date >= from date
        if(leaveRequest.getToDate().isBefore(leaveRequest.getFromDate())){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }

        //Get number of day off requested
        int diff = (int) DAYS.between(leaveRequest.getFromDate(), leaveRequest.getToDate()) + 1;

        ArrayList<PolicyForLeave> policyForLeaves = new ArrayList<>();

        //Get number of day off free check in json file default 3
        Integer numberOfDateFreeCheck = ConstantManager.defaultNumberOfDayOffFreeCheck;
        try {
            //get resource file, create if not exist
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            //get data from resource file
            ObjectMapper mapper = new ObjectMapper().registerModule( new JavaTimeModule());
            List<PolicyForLeave> policyForLeaveList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PolicyForLeave.class));
            if (policyForLeaveList.size() > 0) {
                policyForLeaveList.sort((o1, o2) -> o2.getValidFromDate().compareTo(o1.getValidFromDate()));
                LocalDate fromDate = leaveRequest.getFromDate();
                for (PolicyForLeave item : policyForLeaveList) {
                    if (fromDate.isAfter(item.getValidFromDate())) {
                        if (item.getNumberOfDayOffFreeCheck() != null) {
                            numberOfDateFreeCheck = item.getNumberOfDayOffFreeCheck();
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Check number of day of policy rule
        User user = userService.findByEmail(principal.getName());
        //Deny if exist working task if request days greater than policy free check days
        if (diff > numberOfDateFreeCheck) {
            //check task start in date range request
            if (taskService.existsByExecutorAndStatusAndStartTimeIsBetween(user, "Working", leaveRequest.getFromDate().atStartOfDay(), leaveRequest.getToDate().plusDays(1).atStartOfDay())) {
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
            //check task strat before but still not end
            if (taskService.existsByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(user, "Working", leaveRequest.getFromDate().atStartOfDay())) {
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

    @GetMapping("/search/findByUserAndDateRange")
    public ResponseEntity<List<LeaveRequestSummary>> findByUserAndDateRange(
            @RequestParam String fromDate, @RequestParam String toDate, @RequestParam Integer userId) {

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);

        User user = new User();
        user.setId(userId);

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        //Get all leave request waiting or approved by user
        List<LeaveRequestSummary> leaveRequestSummaries = leaveRequestService.findAllSummaryByUserAndStatusInAndFromDateIsBetween(user, integerList, from, to);
        leaveRequestSummaries.addAll(leaveRequestService.findAllSummaryByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(user, integerList, from));

        if (leaveRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(leaveRequestSummaries);
    }

    @GetMapping("/search/viewLeaves")
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
        for (UserSummary userSummary : userSummaries) {
            UserLeaves userLeaves = new UserLeaves();
            userLeaves.setDisplayName(userSummary.getDisplayName());
            ArrayList<Leave> list = new ArrayList<>();
            User user = new User();
            user.setId(userSummary.getId());
            while (from.plusDays(countPlusDate).isBefore(to)) {
                countPlusDate++;
                LocalDate tmpDate = from.plusDays(countPlusDate);
                Leave leave = new Leave();
                leave.setDate(tmpDate);
                leave.setWaiting(false);
                leave.setApproved(false);
                if (leaveRequestService.existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(tmpDate, tmpDate, user, approvedCode)) {
                    leave.setApproved(true);
                } else if (leaveRequestService.existsLeaveRequestEntitiesByFromDateLessThanEqualAndToDateGreaterThanEqualAndUserAndStatus(tmpDate, tmpDate, user, newCode)) {
                    leave.setWaiting(true);
                }
                list.add(leave);
            }
            countPlusDate = -1;
            userLeaves.setLeaveList(list);
            userLeaveList.add(userLeaves);
        }

        Page<UserLeaves> PageImpl = new PageImpl<UserLeaves>(userLeaveList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), userLeaveList.size());

        if (userSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(PageImpl);
    }

    @GetMapping("/search/notAllowDateFromToday")
    public ResponseEntity<List<LocalDate>> getNotAllowedDateFromToday(Principal principal) {
        //Check number of day of policy rule
        User user = userService.findByEmail(principal.getName());
        LocalDate today = LocalDate.now();
        LocalDate limitDay = LocalDate.now().plusDays(366);

        List<Integer> integerList = new ArrayList<>();
        Integer newCode = Enum.LeaveRequestStatus.New.getLeaveRequestStatusCode();
        integerList.add(newCode);
        Integer approvedCode = Enum.LeaveRequestStatus.Approved.getLeaveRequestStatusCode();
        integerList.add(approvedCode);

        //Get all leave request waiting or approved by user
        List<LeaveRequestSummary> leaveRequests = leaveRequestService.findAllSummaryByUserAndStatusInAndFromDateIsBetween(user, integerList, today, limitDay);
        leaveRequests.addAll(leaveRequestService.findAllSummaryByUserAndStatusInAndFromDateIsBeforeAndToDateIsAfter(user, integerList, today));

        //Make date list from leave request from - to date
        List<LocalDate> result = new ArrayList<>();
        for (LeaveRequestSummary leaveRequest : leaveRequests) {
            LocalDate from = leaveRequest.getFromDate();
            LocalDate to = leaveRequest.getToDate();
            int count = -1;
            while (from.plusDays(count).isBefore(to)) {
                count++;
                result.add(from.plusDays(count));
            }
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/workingTaskDateFromToday")
    public ResponseEntity<List<LocalDate>> getWorkingTaskDateFromToday(Principal principal) {
        //Check number of day of policy rule
        User user = userService.findByEmail(principal.getName());
        LocalDateTime today = LocalDate.now().atStartOfDay();
        //Limit task in 365 days
        LocalDateTime limitDay = LocalDate.now().plusDays(366).atStartOfDay();

        //Get all working task by executor
        List<TaskSummary> taskSummaries = taskService.findAllByExecutorAndStatusAndStartTimeIsBetween(user, "Working", today, limitDay);
        taskSummaries.addAll(taskService.findAllByExecutorAndStatusAndStartTimeIsBeforeAndEndTimeIsAfter(user, "Working", today));
        //Sort by start time
        taskSummaries.sort((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));

        //Declare result list
        List<LocalDate> result = new ArrayList<>();

        //Get working date range from task list
        LocalDate tmpMaxDate = LocalDate.MIN;
        List<LocalDate> tmpStartDateList = new ArrayList<>();
        List<LocalDate> tmpEndDateList = new ArrayList<>();
        int index = -1;
        for (TaskSummary taskSummary : taskSummaries) {
            LocalDate fromDate = taskSummary.getStartTime().toLocalDate();
            LocalDate toDate = taskSummary.getEndTime().toLocalDate();
            //Task list was sorted by start time
            if (fromDate.isAfter(tmpMaxDate)) {
                tmpStartDateList.add(fromDate);
                tmpEndDateList.add(toDate);
                tmpMaxDate = toDate;
                index++;
            } else if (toDate.isAfter(tmpMaxDate)) {
                tmpEndDateList.set(index, toDate);
                tmpMaxDate = toDate;
            }
        }

        //add each day from day ranges to result list
        for ( LocalDate startDateOfRange : tmpStartDateList) {
            LocalDate endDateOfRange = tmpEndDateList.get(tmpStartDateList.indexOf(startDateOfRange));
            int count = -1;
            while (startDateOfRange.plusDays(count).isBefore(endDateOfRange)) {
                count++;
                result.add(startDateOfRange.plusDays(count));
            }
        }
        return ResponseEntity.ok(result);
    }

    private static File getResourceFile(String fileName) throws IOException{
        ClassLoader classloader = ClassLoader.getSystemClassLoader();
        return new File(classloader.getResource(ConstantManager.policyForLeaveConfigFileName).getFile());
    }

    @GetMapping("/search/policyForLeave")
    public ResponseEntity<Page<PolicyForLeave>> getAllPolicyFroLeave(@PageableDefault Pageable pageable){
        try {
            //get resource file, create if not exist
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            //get data from resource file
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
            List<PolicyForLeave> policyForLeaveList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PolicyForLeave.class));
            if (policyForLeaveList.size() > 0) {
                policyForLeaveList.sort((o1, o2) -> o2.getValidFromDate().compareTo(o1.getValidFromDate()));
                Page<PolicyForLeave> PageImpl = new PageImpl<PolicyForLeave>(policyForLeaveList, new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()), policyForLeaveList.size());
                return ResponseEntity.ok(PageImpl);
            }
            return ResponseEntity.noContent().build();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("search/policyForLeave/notAllowDate")
    public ResponseEntity<List<LocalDate>> getNotAllowDateForPolicyManager(){
        List<LocalDate> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        try {
            //get resource file, create if not exist
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            //get data from resource file
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE,true);
            List<PolicyForLeave> policyForLeaveList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PolicyForLeave.class));
            if (policyForLeaveList.size() > 0) {
                for (PolicyForLeave p : policyForLeaveList) {
                    if(!p.getValidFromDate().isBefore(today)){
                        result.add(p.getValidFromDate());
                    }
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/policyForLeave")
    public ResponseEntity<PolicyForLeave> AddPolicyFroLeave(@Valid @RequestBody PolicyForLeave policyForLeave){
        try {
            //Set created and last modified date
            LocalDate today = LocalDate.now();
            policyForLeave.setCreatedDate(today);
            policyForLeave.setLastModifiedDate(today);
            //Validate date from date must after today
            if(policyForLeave.getValidFromDate().isBefore(today)){
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
            //get resource file, create if not exist
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            //get data from resource file
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            List<PolicyForLeave> policyForLeaveList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PolicyForLeave.class));
            //Check exist date valid from
            for (PolicyForLeave p : policyForLeaveList) {
                if(p.getValidFromDate().equals(policyForLeave.getValidFromDate())){
                    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
                }
            }
            //Add and re sort policy list by valid from date
            policyForLeaveList.add(policyForLeave);
            policyForLeaveList.sort((o1, o2) -> o2.getValidFromDate().compareTo(o1.getValidFromDate()));
            mapper.writeValue(file, policyForLeaveList);
            return ResponseEntity.ok(policyForLeave);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/policyForLeave")
    public ResponseEntity<PolicyForLeave> EditPolicyFroLeave(@Valid @RequestBody PolicyForLeave oldPolicyForLeave,PolicyForLeave newPolicyForLeave ){
        try {
            //Set last modified time
            LocalDate today = LocalDate.now();
            newPolicyForLeave.setLastModifiedDate(today);
            //Validate date from date must after today
            if(newPolicyForLeave.getValidFromDate().isBefore(today)){
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
            //get resource file, create if not exist
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            //get data from resource file
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            List<PolicyForLeave> policyForLeaveList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PolicyForLeave.class));
            //Check exist date valid from
            for (PolicyForLeave p : policyForLeaveList) {
                if(p.getValidFromDate().equals(newPolicyForLeave.getValidFromDate())){
                    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
                }
            }
            //find index of old and replace by new one
            int index = policyForLeaveList.indexOf(oldPolicyForLeave);
            policyForLeaveList.set(index,newPolicyForLeave);
            //re-sort
            policyForLeaveList.sort((o1, o2) -> o2.getValidFromDate().compareTo(o1.getValidFromDate()));
            mapper.writeValue(file, policyForLeaveList);
            return ResponseEntity.ok(newPolicyForLeave);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/policyForLeave")
    public ResponseEntity EditPolicyFroLeave(@Valid @RequestBody PolicyForLeave policyForLeave){
        try {
            LocalDate today = LocalDate.now();
            //Validate date from date must after today
            if(!policyForLeave.getValidFromDate().isAfter(today)){
                return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
            }
            //get resource file, create if not exist
            File file = getResourceFile(ConstantManager.policyForLeaveConfigFileName);
            //get data from resource file
            ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
            mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            List<PolicyForLeave> policyForLeaveList = mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, PolicyForLeave.class));
            //remove the policy
            policyForLeaveList.remove(policyForLeave);
            mapper.writeValue(file, policyForLeaveList);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.noContent().build();
    }
}
