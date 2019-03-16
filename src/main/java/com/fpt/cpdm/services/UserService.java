package com.fpt.cpdm.services;

import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.users.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {


    UserDetail save(User user, Principal principal);

    UserDetail findDetailById(Integer id, Principal principal);

    UserDetail findDetailByEmail(String email);

    List<User> findAllByDisplayNameContaining(String displayName);

    List<UserForSelect> findAllForSelectByEmailContains(String email);

    List<UserSummary> findAllSummaryRelatedByTask_Id(Integer id);

    User findByEmail(String email);

    UserBasic findBasicByEmail(String email);

    List<UserDisplayName> findDisplayNameByDepartmentAndRole_Name(Department department, String roleName);

    Page<UserSummary> findSummaryByDepartmentAndRole_Name(Department department, String roleName, Pageable pageable);

    Page<UserSummary> findAllSummaryForAdmin(Pageable pageable);
}
