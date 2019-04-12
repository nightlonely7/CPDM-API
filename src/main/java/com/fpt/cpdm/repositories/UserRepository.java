package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.users.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<List<UserEntity>> findAllByDisplayNameContaining(String displayName);

    Optional<UserDetail> findDetailById(Integer id);

    Optional<UserEntity> findById(Integer id);

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

    Page<UserSummary> findAllSummaryByEmailContainingAndDepartment_Id(String email, Integer depId, Pageable pageable);

    Page<UserSummary> findAllSummaryByEmailContainingAndDepartment_IdAndGender(String email, Integer depId, Boolean gender, Pageable pageable);

    Page<UserSummary> findAllSummaryByDisplayNameContainingAndDepartment_Id(String displayName, Integer depId, Pageable pageable);

    Page<UserSummary> findAllSummaryByDisplayNameContainingAndDepartment_IdAndGender(String displayName, Integer depId, Boolean gender, Pageable pageable);

    Page<UserSummary> findAllSummaryByFullNameContainingAndDepartment_Id(String fullName, Integer depId, Pageable pageable);

    Page<UserSummary> findAllSummaryByFullNameContainingAndDepartment_IdAndGender(String fullName, Integer depId, Boolean gender, Pageable pageable);

    @Query("select user from User user where " +
            "(:birthdayFrom is null or user.birthday >= :birthdayFrom) and " +
            "(:birthdayTo is null or user.birthday <= :birthdayTo)")
    Page<UserSummary> findAllSummaryByAge(@Param("birthdayFrom") LocalDate birthdayFrom,
                                          @Param("birthdayTo") LocalDate birthdayTo,
                                          Pageable pageable);

    @Query("select user from User user where " +
            "(:birthdayFrom is null or user.birthday >= :birthdayFrom) and " +
            "(:birthdayTo is null or user.birthday <= :birthdayTo) and " +
            "(:gender is null or user.gender = :gender)")
    Page<UserSummary> findAllSummaryByAgeAndGender(@Param("birthdayFrom") LocalDate birthdayFrom,
                                                   @Param("birthdayTo") LocalDate birthdayTo,
                                                   @Param("gender") Boolean gender, Pageable pageable);

    Optional<UserBirthDate> findFirstBirthDateByBirthdayNotNullOrderByBirthdayDesc();

    Optional<UserBirthDate> findFirstBirthDateByBirthdayNotNullOrderByBirthdayAsc();

    Boolean existsByDepartment_Id(Integer id);

    List<UserDisplayName> findAllDisplayNameByDepartmentAndRole_NameAndIdNot(DepartmentEntity departmentEntity, String roleName, Integer id);

    List<UserDisplayName> findAllDisplayNameByRole_Name(String roleName);
}
