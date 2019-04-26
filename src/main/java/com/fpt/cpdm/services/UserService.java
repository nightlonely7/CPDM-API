package com.fpt.cpdm.services;

import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.users.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.jws.soap.SOAPBinding;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserDetail save(User user, Principal principal);

    UserDetail create(User user, Principal principal);

    UserDetail personalUpdate(User user, Principal principal);

    UserDetail update(User user, Principal principal);

    User delete(Integer id);

    UserDetail findDetailById(Integer id, Principal principal);

    UserDetail findDetailByEmail(String email);

    List<User> findAllByDisplayNameContaining(String displayName);

    List<UserForSelect> findAllForSelectByEmailContains(String email);

    List<UserSummary> findAllManagerForSelect();

    List<UserSummary> findAllSummaryRelatedByTask_Id(Integer id);

    User findByEmail(String email);

    UserBasic findBasicByEmail(String email);

    List<UserDisplayName> findDisplayNameByDepartmentAndRole_Name(Department department, String roleName);

    Page<UserSummary> findSummaryByDepartmentAndRole_Name(Department department, String roleName, Pageable pageable);

    Page<UserSummary> findAllSummaryForAdmin(Pageable pageable);

    List<UserBirthDate> findMaxAndMinAge();

    Boolean existsByEmail(String email);

    List<UserDisplayName> findDisplayNameByDepartmentAndRole_NameAndIdNot(Department department, String roleName, Integer id);

    List<UserDisplayName> findAllDisplayNameByRole_Name(String roleName);

    List<UserSummary> findAllSummaryByDepartmentId(Integer departmentId);

    Page<UserSummary> advancedSearch(String email, String displayName, String fullName, Integer departmentId,
                                       LocalDate birthDateFrom, LocalDate birthDateTo, Boolean gender, Pageable pageable);
}
