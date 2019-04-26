package com.fpt.cpdm.controllers;

import com.fpt.cpdm.exceptions.ModelNotValidException;
import com.fpt.cpdm.models.PushNotification;
import com.fpt.cpdm.models.notifications.Notification;
import com.fpt.cpdm.models.notifications.NotificationSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.services.NotificationService;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelErrorMessage;
import com.fpt.cpdm.utils.PushNotificationServiceImpl;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationSummary>> findAllByCurrentLoogedUser(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        List<NotificationSummary> notificationSummaries = notificationService.findAllByCurrentLoogedUser(user);

        return ResponseEntity.ok(notificationSummaries);
    }

    @PostMapping()
    public ResponseEntity<Notification> create(@Valid @RequestBody Notification notification,
                                               BindingResult result, Principal principal) {
        return save(null, notification, result, principal);
    }

    @PostMapping("/push")
    public void pushNotification(@Valid @RequestBody PushNotification data) {
        try {
            notificationService.pushNotification(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @PutMapping("/read/{id}")
    public ResponseEntity<Notification> read(@PathVariable(name = "id") Integer id) {
        //get notification by id
        Notification notification = notificationService.findById(id);
        notification.setRead(true);
        Notification savedNotification = notificationService.save(notification);
        return ResponseEntity.ok(savedNotification);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Notification> edit(@PathVariable(name = "id") Integer id,
                                             @Valid @RequestBody Notification notification,
                                             BindingResult result, Principal principal) {
        return save(id, notification, result, principal);
    }

    private ResponseEntity<Notification> save(Integer id, Notification notification, BindingResult result, Principal principal) {
        if (result.hasErrors()) {
            String message = ModelErrorMessage.build(result);
            throw new ModelNotValidException(message);
        }
        User user = userService.findByEmail(principal.getName());

        notification.setId(id);
        notification.setCreator(user);
        Notification savedNotification = notificationService.save(notification);
        return ResponseEntity.ok(savedNotification);
    }
}
