package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.entities.RoleEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.users.UserBasic;
import com.fpt.cpdm.models.users.UserDisplayName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<List<UserEntity>> findAllByDisplayNameContaining(String displayName);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserBasic> findBasicByEmail(String email);

    boolean existsByEmail(String email);

    UserDisplayName findDisplayNameByEmail(String email);

    List<UserDisplayName> findUserDisplayNameByDepartmentAndRole(DepartmentEntity departmentEntity, RoleEntity roleEntity);
}
