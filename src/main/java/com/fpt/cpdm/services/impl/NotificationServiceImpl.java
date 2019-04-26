package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.NotificationEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.notifications.NotificationNotFoundException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.PushNotification;
import com.fpt.cpdm.models.notifications.Notification;
import com.fpt.cpdm.models.notifications.NotificationSummary;
import com.fpt.cpdm.models.users.User;
import com.fpt.cpdm.repositories.NotificationRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.NotificationService;
import com.fpt.cpdm.utils.ConstantManager;
import com.fpt.cpdm.utils.ModelConverter;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<NotificationSummary> findAllByCurrentLoogedUser(User user) {
        UserEntity userEntity = ModelConverter.userModelToEntity(user);
        return notificationRepository.findAllByUserAndHiddenOrderByCreatedTimeDesc(userEntity, false);
    }

    @Override
    public void pushNotification(PushNotification pushNotification) throws JSONException {
        JSONObject msg = new JSONObject();
        msg.put("title", pushNotification.getTitle());
        msg.put("body", pushNotification.getDetail());
        msg.put("url", pushNotification.getUrl());
        msg.put("id", pushNotification.getId());

        pushNotification.getKeys().forEach(key -> {
            try {
                callToFcmServer(msg, key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private void callToFcmServer(JSONObject message, String receiverFcmKey) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "key=" + ConstantManager.FIREBASE_SERVER_KEY);
        httpHeaders.set("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        json.put("data", message);
        json.put("notification", message);
        json.put("to", receiverFcmKey);

        HttpEntity<String> httpEntity = new HttpEntity<>(json.toString(), httpHeaders);
        restTemplate.postForObject(ConstantManager.FIREBASE_API_URL, httpEntity, String.class);
    }

    @Override
    public Notification save(Notification model) {
        if (!userRepository.existsById(model.getUser().getId()) || !userRepository.existsById(model.getCreator().getId())) {
            throw new UserNotFoundException(model.getUser().getId());
        }
        NotificationEntity notificationEntity = ModelConverter.notificationModelToEntity(model);
        NotificationEntity savedNotificationEntity = notificationRepository.save(notificationEntity);
        return ModelConverter.notificationEntityToModel(savedNotificationEntity);
    }

    @Override
    public List<Notification> saveAll(List<Notification> models) {
        return null;
    }

    @Override
    public Notification findById(Integer id) {
        NotificationEntity notificationEntity = notificationRepository.findById(id).orElseThrow(
                () -> new NotificationNotFoundException(id)
        );
        return ModelConverter.notificationEntityToModel(notificationEntity);
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public List<Notification> findAll() {
        return null;
    }

    @Override
    public List<Notification> findAllById(List<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void delete(Notification model) {

    }

    @Override
    public void deleteAll(List<Notification> models) {

    }

    @Override
    public void deleteAll() {

    }
}
