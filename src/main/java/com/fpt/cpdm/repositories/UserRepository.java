package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.entities.RoleEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.users.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<List<UserEntity>> findAllByDisplayNameContaining(String displayName);

    Optional<UserDetail> findDetailById(Integer id);

    Optional<UserEntity> findById(Integer id);

    Optional<UserDisplayName> findDisplayNameById(Integer id);

    Optional<UserDetail> findDetailByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserBasic> findBasicByEmail(String email);

    boolean existsByEmail(String email);

    UserDisplayName findDisplayNameByEmail(String email);

    List<UserSummary> findAllSummaryByRole_Name(String roleName);

    List<UserDisplayName> findDisplayNameByDepartmentAndRole_Name(DepartmentEntity departmentEntity, String roleName);

    List<UserForSelect> findAllForSelectByEmailContainsAndEnabledIsTrue(String email);

    List<UserSummary> findAllSummaryByRelatedTasksAndEnabledTrue(TaskEntity taskEntity);

    Page<UserSummary> findSummaryByDepartmentAndRole_Name(DepartmentEntity departmentEntity, String roleName, Pageable pageable);

    Page<UserSummary> findAllSummaryBy(Pageable pageable);

    @Query("select user from User user where " +
            "(:email is null or lower(user.email) like %:email%) and" +
            "(:displayName is null or lower(user.displayName) like %:displayName%) and " +
            "(:fullName is null or lower(user.fullName) like %:fullName%) and " +
            "(:departmentId is null or user.department.id = :departmentId) and " +
            "(:birthdayFrom is null or user.birthday >= :birthdayFrom) and " +
            "(:birthdayTo is null or user.birthday <= :birthdayTo) and " +
            "(:gender is null or user.gender = :gender)")
    Page<UserSummary> advancedSearch (@Param("email") String email,
                                      @Param("displayName") String displayName,
                                      @Param("fullName") String fullName,
                                      @Param("departmentId") Integer departmentId,
                                      @Param("birthdayFrom") LocalDate birthdayFrom,
                                      @Param("birthdayTo") LocalDate birthdayTo,
                                      @Param("gender") Boolean gender, Pageable pageable);

    Optional<UserBirthDate> findFirstBirthDateByBirthdayNotNullOrderByBirthdayDesc();

    Optional<UserBirthDate> findFirstBirthDateByBirthdayNotNullOrderByBirthdayAsc();

    Boolean existsByDepartment_Id(Integer id);

    Boolean existsByIdAndDepartment_Id(Integer id, Integer departmentId);

    Boolean existsByIdAndRole_Id(Integer id, Integer roleId);

    List<UserDisplayName> findAllDisplayNameByDepartmentAndRole_NameAndIdNot(DepartmentEntity departmentEntity, String roleName, Integer id);

    List<UserDisplayName> findAllDisplayNameByRole_Name(String roleName);

    List<UserSummary> findAllSummaryByDepartment_IdAndRole_IdNotLike(Integer departmentId, Integer roleId);

    List<UserSummary> findAllSummaryByDepartment_IdAndRole_Id(Integer departmentId, Integer roleId);

    Page<UserSummary> findAllByRole_IdIn(List<Integer> listRole, Pageable pageable);
}
