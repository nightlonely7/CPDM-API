package com.fpt.cpdm.utils;


import com.fpt.cpdm.entities.DocumentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.Document;
import com.fpt.cpdm.models.Task;
import com.fpt.cpdm.models.User;
import org.modelmapper.ModelMapper;


public class ModelConverter {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static User userEntityToModel(UserEntity userEntity) {
        User user = MODEL_MAPPER.map(userEntity, User.class);
        user.getRole().setName(user.getRole().getName().substring(5));
        user.setPassword(null);
        return user;
    }

    public static UserEntity userModelToEntity(User user) {
        UserEntity userEntity = MODEL_MAPPER.map(user, UserEntity.class);
        return userEntity;
    }

    public static Task taskEntityToModel(TaskEntity taskEntity) {
        Task task = MODEL_MAPPER.map(taskEntity, Task.class);
        return task;
    }

    public static TaskEntity taskModelToEntity(Task task) {
        TaskEntity taskEntity = MODEL_MAPPER.map(task, TaskEntity.class);
        return taskEntity;
    }

    public static Document documentEntityToModel(DocumentEntity documentEntity) {
        Document document = MODEL_MAPPER.map(documentEntity, Document.class);
        return document;
    }

    public static DocumentEntity documentModelToEntity(Document document) {
        DocumentEntity documentEntity = MODEL_MAPPER.map(document, DocumentEntity.class);
        return documentEntity;
    }
}
