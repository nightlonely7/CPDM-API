package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.entities.RoleEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.departments.DepartmentDTO;
import com.fpt.cpdm.models.users.UserBasic;
import com.fpt.cpdm.models.users.UserDetail;
import com.fpt.cpdm.models.users.UserDisplayName;
import com.fpt.cpdm.models.users.UserSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<List<UserEntity>> findAllByDisplayNameContaining(String displayName);

    Optional<UserDetail> findDetailById(Integer id);

    Optional<UserDetail> findDetailByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserBasic> findBasicByEmail(String email);

    boolean existsByEmail(String email);

    UserDisplayName findDisplayNameByEmail(String email);

    List<UserDisplayName> findDisplayNameByDepartmentAndRole_Name(DepartmentEntity departmentEntity, String roleName);

    Page<UserSummary> findSummaryByDepartmentAndRole_Name(DepartmentEntity departmentEntity, String roleName, Pageable pageable);
}
