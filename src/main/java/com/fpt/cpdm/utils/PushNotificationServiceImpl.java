package com.fpt.cpdm.utils;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class PushNotificationServiceImpl {
    private final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
    private final String FIREBASE_SERVER_KEY = "AAAALNikUl4:APA91bEC0s3Ua-44isrp19yL_k4XeORndrVawsqnwrTylsVXAe9NUA0yMkMEk0FXN9Oi7EY98D3j8PwSt_LQU4PQWbGM_qskrXofxBINJtD3L5Sd71ucGjoxkP1Lr4sbAsHsemeQ8Ekr";


    public void sendPushNotification(List<String> keys, String messageTitle, String message) throws JSONException {


        JSONObject msg = new JSONObject();

        msg.put("title", messageTitle);
        msg.put("body", message);
        msg.put("notificationType", "Test");

        keys.forEach(key -> {
            System.out.println("\nCalling fcm Server >>>>>>>");
            String response = null;
            try {
                response = callToFcmServer(msg, key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("Got response from fcm Server : " + response + "\n\n");
        });

    }

    private String callToFcmServer(JSONObject message, String receiverFcmKey) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + FIREBASE_SERVER_KEY);
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();

        json.put("data", message);
        json.put("notification", message);
        json.put("to", receiverFcmKey);

        System.out.println("Sending :" + json.toString());

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        return restTemplate.postForObject(FIREBASE_API_URL, httpEntity, String.class);
    }
}
