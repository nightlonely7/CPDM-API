package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.askingRequests.AskingRequest;
import com.fpt.cpdm.models.askingRequests.AskingRequestSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.AskingRequestService;
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

@RestController
@RequestMapping("/askingRequests")
public class AskingRequestController {
    private AskingRequestService askingRequestService;
    private UserService userService;

    @Autowired
    public AskingRequestController(AskingRequestService askingRequestService, UserService userService) {
        this.askingRequestService = askingRequestService;
        this.userService = userService;
    }

    @GetMapping("/search/findByUser")
    public ResponseEntity<Page<AskingRequestSummary>> findbyUser(@RequestParam Integer status,
                                                                 @PageableDefault Pageable pageable,
                                                                 Principal principal) {
        // get current logged user
        User user = userService.findByEmail(principal.getName());

        Page<AskingRequestSummary> askingRequestSummaries = askingRequestService.findAllSummaryByUserAndStatus(user, status, pageable);
        if (askingRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(askingRequestSummaries);
    }

    @GetMapping("/search/findByReceiver")
    public ResponseEntity<Page<AskingRequestSummary>> findByReceiver(@RequestParam Integer status,
                                                                     @PageableDefault Pageable pageable,
                                                                     Principal principal) {
        // get current logged user
        User receiver = userService.findByEmail(principal.getName());

        Page<AskingRequestSummary> askingRequestSummaries = askingRequestService.findAllSummaryByReceiverAndStatus(receiver, status, pageable);
        if (askingRequestSummaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(askingRequestSummaries);
    }

    @PostMapping
    public ResponseEntity<AskingRequest> create(@Valid @RequestBody AskingRequest askingRequest,
                                                BindingResult result, Principal principal) {

        User user = userService.findByEmail(principal.getName());
        askingRequest.setUser(user);

        return save(null, askingRequest, result, principal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AskingRequest> edit(@PathVariable(name = "id") Integer id,
                                              @Valid @RequestBody AskingRequest askingRequest, BindingResult result, Principal principal) {

        if (askingRequestService.findById(id).getStatus() == Enum.AskingRequestStatus.Replied.getAskingRequestStatusCode()){
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }

        return save(id, askingRequest, result, principal);
    }

    private ResponseEntity<AskingRequest> save(Integer id, AskingRequest askingRequest, BindingResult result, Principal principal) {

        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }

        askingRequest.setId(id);

        AskingRequest savedAskingRequest = askingRequestService.save(askingRequest);

        return ResponseEntity.ok(savedAskingRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Integer id) {
        if (askingRequestService.findById(id).getStatus() == Enum.AskingRequestStatus.Replied.getAskingRequestStatusCode()) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
        }
        askingRequestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
