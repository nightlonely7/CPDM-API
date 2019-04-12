package com.fpt.cpdm.controllers;

import com.fpt.cpdm.models.PushNotification;
import com.fpt.cpdm.utils.PushNotificationServiceImpl;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private  PushNotificationServiceImpl pushNotificationService = new PushNotificationServiceImpl();

    @PostMapping("/push")
    public void pushNotification(@Valid @RequestBody PushNotification data){
        try {
            pushNotificationService.sendPushNotification(data.getKeys(),data.getTitle(),data.getDetail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
