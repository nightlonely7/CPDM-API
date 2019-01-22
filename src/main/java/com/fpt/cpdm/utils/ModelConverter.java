package com.fpt.cpdm.utils;


import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.User;
import org.modelmapper.ModelMapper;


public class ModelConverter {

    public static User userEntityToModel(UserEntity userEntity) {
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(userEntity, User.class);
        user.setRole(userEntity.getRole().getName().substring(5));
        user.setPassword(null);
        return user;
    }

    public static UserEntity userModelToEntity(User user) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(user, UserEntity.class);
        return userEntity;
    }


}
