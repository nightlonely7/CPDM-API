package com.fpt.cpdm.services.impl;

import com.fpt.cpdm.entities.DepartmentEntity;
import com.fpt.cpdm.entities.RoleEntity;
import com.fpt.cpdm.entities.TaskEntity;
import com.fpt.cpdm.entities.UserEntity;
import com.fpt.cpdm.exceptions.UnauthorizedException;
import com.fpt.cpdm.exceptions.departments.DepartmentAlreadyHaveManagerException;
import com.fpt.cpdm.exceptions.roles.RoleNotFoundException;
import com.fpt.cpdm.exceptions.tasks.TaskNotFoundException;
import com.fpt.cpdm.exceptions.users.UserEmailDuplicateException;
import com.fpt.cpdm.exceptions.users.UserNotFoundException;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.users.*;
import com.fpt.cpdm.repositories.RoleRepository;
import com.fpt.cpdm.repositories.TaskRepository;
import com.fpt.cpdm.repositories.UserRepository;
import com.fpt.cpdm.services.UserService;
import com.fpt.cpdm.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TaskRepository taskRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository, TaskRepository taskRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> optional = userRepository.findByEmail(username);
        return optional.orElseThrow(
                () -> new UsernameNotFoundException("User with email " + username + " not found!")
        );
    }

    @Override
    public UserDetail save(User user, Principal principal) {

        // check id exist (no need for create)
        if (user.getId() != null && userRepository.existsById(user.getId()) == false) {
            throw new UserNotFoundException(user.getId());
        }

        // get creator/editor
        UserEntity creator = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UsernameNotFoundException(principal.getName())
        );

        // check creator not staff
        if (user.getId() == null && creator.getRole().getName().equals("ROLE_STAFF")) {
            throw new UnauthorizedException();
        }

        // check email duplicate
        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailDuplicateException(user.getEmail());
        }

        // for creating
        if (user.getId() == null) {
            user.setDisplayName(user.getEmail());
        }

        // set department and role if creator is manager (no need for admin)
        if (creator.getRole().getName().equals("ROLE_MANAGER")) {

            Department department = new Department();
            department.setId(creator.getDepartment().getId());
            user.setDepartment(department);

            Role role = new Role();
            role.setId(1); // id 1 for STAFF
            user.setRole(role);
        }

        // check department is already have a manager
//        if (userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
//            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
//        }
        if (user.getRole().getId() == 2 // 1 for "ROLE_STAFF"
                && userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
        }

        // encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        UserEntity userEntity = ModelConverter.userModelToEntity(user);

        // set role
        RoleEntity roleEntity = roleRepository.findById(userEntity.getRole().getId()).orElseThrow(
                () -> new RoleNotFoundException(userEntity.getRole().getId())
        );
        userEntity.setRole(roleEntity);

        System.out.println(userEntity.isEnabled());
        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserDetail savedUserDetail = userRepository.findDetailById(savedUserEntity.getId()).get();

        return savedUserDetail;
    }

    @Override
    public UserDetail create(User user, Principal principal) {
// check id exist (no need for create)
        if (user.getId() != null && userRepository.existsById(user.getId()) == false) {
            throw new UserNotFoundException(user.getId());
        }

        // get creator/editor
        UserEntity creator = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UsernameNotFoundException(principal.getName())
        );

        // check creator not staff
        if (user.getId() == null && creator.getRole().getName().equals("ROLE_STAFF")) {
            throw new UnauthorizedException();
        }

        // check email duplicate
        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailDuplicateException(user.getEmail());
        }

        // for creating
        if (user.getId() == null) {
            user.setDisplayName(user.getEmail());
        }

        // set department and role if creator is manager (no need for admin)
        if (creator.getRole().getName().equals("ROLE_MANAGER")) {

            Department department = new Department();
            department.setId(creator.getDepartment().getId());
            user.setDepartment(department);

            Role role = new Role();
            role.setId(1); // id 1 for STAFF
            user.setRole(role);
        }

        // check department is already have a manager
//        if (userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
//            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
//        }
        if (user.getRole().getId() == 2 // 1 for "ROLE_STAFF", 2 1 for "ROLE_MANAGER"
                && userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
        }

        // encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        UserEntity userEntity = ModelConverter.userModelToEntity(user);

        // set role
        RoleEntity roleEntity = roleRepository.findById(userEntity.getRole().getId()).orElseThrow(
                () -> new RoleNotFoundException(userEntity.getRole().getId())
        );
        userEntity.setRole(roleEntity);

        System.out.println(userEntity.isEnabled());
        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserDetail savedUserDetail = userRepository.findDetailById(savedUserEntity.getId()).get();

        return savedUserDetail;
    }

    @Override
    public UserDetail personalUpdate(User user, Principal principal) {

        // check id exist (no need for create)
        if (user.getId() != null && userRepository.existsById(user.getId()) == false) {
            throw new UserNotFoundException(user.getId());
        }

        // get creator/editor
        UserEntity creator = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UsernameNotFoundException(principal.getName())
        );

        // check creator not staff
        if (user.getId() == null && creator.getRole().getName().equals("ROLE_STAFF")) {
            throw new UnauthorizedException();
        }

        // check email duplicate
        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailDuplicateException(user.getEmail());
        }

        // for creating
        if (user.getId() == null) {
            user.setDisplayName(user.getEmail());
        }

        // set department and role if creator is manager (no need for admin)
        if (creator.getRole().getName().equals("ROLE_MANAGER")) {

            Department department = new Department();
            department.setId(creator.getDepartment().getId());
            user.setDepartment(department);

            Role role = new Role();
            role.setId(1); // id 1 for STAFF
            user.setRole(role);
        }

        // check department is already have a manager
//        if (userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
//            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
//        }
//        if (user.getRole().getId() == 1 // 1 for "ROLE_STAFF"
//                && userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
//            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
//        }

        UserEntity userEntity = ModelConverter.userModelToEntity(user);

        // set role
        RoleEntity roleEntity = roleRepository.findById(userEntity.getRole().getId()).orElseThrow(
                () -> new RoleNotFoundException(userEntity.getRole().getId())
        );
        userEntity.setRole(roleEntity);

        System.out.println("password: " + userEntity.getPassword());

        userEntity.setDisplayName(user.getDisplayName());
        userEntity.setAddress(user.getAddress());
        userEntity.setBirthday(user.getBirthday());
        userEntity.setEmail(user.getEmail());
        userEntity.setGender(user.getGender());
        userEntity.setPhone(user.getPhone());

        // encode password
        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
        System.out.println("password: " + encodedPassword);
        userEntity.setPassword(encodedPassword);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserDetail savedUserDetail = userRepository.findDetailById(savedUserEntity.getId()).get();

        return savedUserDetail;
    }

    @Override
    public UserDetail update(User user, Principal principal) {

        // check id exist (no need for create)
        if (user.getId() != null && userRepository.existsById(user.getId()) == false) {
            throw new UserNotFoundException(user.getId());
        }

        // get creator/editor
        UserEntity creator = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UsernameNotFoundException(principal.getName())
        );

        // check creator not staff
        if (user.getId() == null && creator.getRole().getName().equals("ROLE_STAFF")) {
            throw new UnauthorizedException();
        }

        // check email duplicate
        if (user.getId() == null && userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailDuplicateException(user.getEmail());
        }

        // for creating
//        if (user.getId() == null) {
//            user.setDisplayName(user.getEmail());
//        }

        // set department and role if creator is manager (no need for admin)
//        if (creator.getRole().getName().equals("ROLE_MANAGER")) {
//
//            Department department = new Department();
//            department.setId(creator.getDepartment().getId());
//            user.setDepartment(department);
//
//            Role role = new Role();
//            role.setId(1); // id 1 for STAFF
//            user.setRole(role);
//        }

        // check department is already have a manager
//        if (userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
//            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
//        }
//        if (user.getRole().getId() == 1 // 1 for "ROLE_STAFF"
//                && userRepository.existsByDepartment_Id(user.getDepartment().getId())) {
//            throw new DepartmentAlreadyHaveManagerException("This department already have a manager!");
//        }

        UserEntity userEntity = ModelConverter.userModelToEntity(user);

        // set role
        RoleEntity roleEntity = roleRepository.findById(userEntity.getRole().getId()).orElseThrow(
                () -> new RoleNotFoundException(userEntity.getRole().getId())
        );
//        userEntity.setRole(roleEntity);

        userEntity.setRole(roleEntity);

        userEntity.setEmail(user.getEmail());

        // encode password
//        String encodedPassword = passwordEncoder.encode(userEntity.getPassword());
//        System.out.println("password: " + encodedPassword);
//        userEntity.setPassword(encodedPassword);

//      UserEntity savedUserEntity = userRepository.save(userEntity);

        UserEntity savedUserEntity = userRepository.save(userEntity);

        UserDetail savedUserDetail = userRepository.findDetailById(savedUserEntity.getId()).get();

        return savedUserDetail;
    }

    @Override
    public User delete(Integer id) {
        Optional<UserEntity> userEntity = userRepository.findById(id);
        if(userEntity.isPresent()){
            userEntity.get().setEnabled(false);
            userRepository.save(userEntity.get());
        }
        User savedUser = ModelConverter.userEntityToModel(userEntity.get());
        return savedUser;
    }

    private List<User> getUsersConverted(List<UserEntity> userEntities) {

        List<User> users = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            User user = ModelConverter.userEntityToModel(userEntity);
            users.add(user);
        }

        return users;
    }

    private List<UserEntity> getUserEntitiesConverted(List<User> users) {

        List<UserEntity> userEntities = new ArrayList<>();
        for (User user : users) {
            UserEntity userEntity = ModelConverter.userModelToEntity(user);
            userEntities.add(userEntity);
        }

        return userEntities;
    }

    @Override
    public UserDetail findDetailById(Integer id, Principal principal) {

        // get API taker
        UserEntity taker = userRepository.findByEmail(principal.getName()).orElseThrow(
                () -> new UsernameNotFoundException(principal.getName())
        );

        // check if user is not staff
        if (taker.getRole().getName().equals("ROLE_STAFF")) {
            throw new UnauthorizedException();
        }

        // get user entity
        UserEntity userEntity = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException(id)
        );

        // check if manager is the same department with staff (admin no need to check)
        if (taker.getRole().getName().equals("ROLE_MANAGER")
                && taker.getDepartment().equals(userEntity.getDepartment()) == false) {
            throw new UnauthorizedException();
        }

        // get user detail
        Optional<UserDetail> optional = userRepository.findDetailById(id);
        UserDetail userDetail = optional.orElseThrow(
                () -> new UserNotFoundException(id)
        );

        return userDetail;
    }

    @Override
    public UserDetail findDetailByEmail(String email) {

        Optional<UserDetail> optional = userRepository.findDetailByEmail(email);
        UserDetail userDetail = optional.orElseThrow(
                () -> new UsernameNotFoundException(email)
        );

        return userDetail;
    }

    @Override
    public List<User> findAllByDisplayNameContaining(String displayName) {
        Optional<List<UserEntity>> optional = userRepository.findAllByDisplayNameContaining(displayName);
        List<UserEntity> userEntities = optional.get();
        List<User> users = getUsersConverted(userEntities);
        return users;
    }

    @Override
    public List<UserForSelect> findAllForSelectByEmailContains(String email) {
        return userRepository.findAllForSelectByEmailContainsAndEnabledIsTrue(email);
    }

    @Override
    @Secured("ROLE_ADMIN")
    public List<UserSummary> findAllManagerForSelect() {
        return userRepository.findAllSummaryByRole_Name("ROLE_MANAGER");
    }

    @Override
    public List<UserSummary> findAllSummaryRelatedByTask_Id(Integer id) {
        TaskEntity taskEntity = taskRepository.findById(id).orElseThrow(
                () -> new TaskNotFoundException(id)
        );

        return userRepository.findAllSummaryByRelatedTasksAndEnabledTrue(taskEntity);
    }

    @Override
    public User findByEmail(String email) {

        Optional<UserEntity> optional = userRepository.findByEmail(email);
        UserEntity userEntity = optional.orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found!"));
        User user = ModelConverter.userEntityToModel(userEntity);

        return user;
    }

    @Override
    public UserBasic findBasicByEmail(String email) {

        Optional<UserBasic> optional = userRepository.findBasicByEmail(email);
        UserBasic userBasic = optional.orElseThrow(
                () -> new UsernameNotFoundException("User with email " + email + " not found!"));

        return userBasic;
    }

    @Override
    public List<UserDisplayName> findDisplayNameByDepartmentAndRole_Name(Department department, String roleName) {

        DepartmentEntity departmentEntity = ModelConverter.departmentModelToEntity(department);
        List<UserDisplayName> userDisplayNames = userRepository.findDisplayNameByDepartmentAndRole_Name(
                departmentEntity, roleName);

        return userDisplayNames;
    }

    @Override
    public Page<UserSummary> findSummaryByDepartmentAndRole_Name(Department department, String roleName, Pageable pageable) {
        DepartmentEntity departmentEntity = ModelConverter.departmentModelToEntity(department);
        Page<UserSummary> userSummaries = userRepository.findSummaryByDepartmentAndRole_Name(
                departmentEntity, roleName, pageable);

        return userSummaries;

    }

    @Override
    public Page<UserSummary> findAllSummaryForAdmin(Pageable pageable) {
        return userRepository.findAllSummaryBy(pageable);
    }

    @Override
    public List<UserBirthDate> findMaxAndMinAge() {
        Optional<UserBirthDate> minAgeResult = userRepository.findFirstBirthDateByBirthdayNotNullOrderByBirthdayAsc();
        Optional<UserBirthDate> maxAgeResult = userRepository.findFirstBirthDateByBirthdayNotNullOrderByBirthdayDesc();
        UserBirthDate maxAge = null;
        UserBirthDate minAge = null;
        if (maxAgeResult.isPresent()) {
            maxAge = maxAgeResult.get();
        }
        if (minAgeResult.isPresent()) {
            minAge = minAgeResult.get();
        }
        List<UserBirthDate> userBirthDates = new ArrayList<>();
        userBirthDates.add(minAge);
        userBirthDates.add(maxAge);
        return userBirthDates;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<UserDisplayName> findDisplayNameByDepartmentAndRole_NameAndIdNot(Department department, String roleName, Integer id) {
        DepartmentEntity departmentEntity = ModelConverter.departmentModelToEntity(department);
        List<UserDisplayName> userDisplayNames = userRepository.findAllDisplayNameByDepartmentAndRole_NameAndIdNot(departmentEntity, roleName,id);
        return userDisplayNames;
    }

    @Override
    public List<UserDisplayName> findAllDisplayNameByRole_Name(String roleName) {
        return userRepository.findAllDisplayNameByRole_Name(roleName);
    }

    @Override
    public List<UserSummary> findAllSummaryByDepartmentId(Integer departmentId) {
        return userRepository.findAllSummaryByDepartment_IdAndRole_IdNotLike(departmentId, 3);
    }

    @Override
    public Page<UserSummary> findAllByRole_IdIn(List<Integer> listRole, Pageable pageable) {
        return userRepository.findAllByRole_IdIn(listRole,pageable);
    }

    @Override
    public Page<UserSummary> findAllByRole_IdInAndDepartment_IdIn(List<Integer> listRole, List<Integer> listDepartment, Pageable pageable) {
        return userRepository.findAllByRole_IdInAndDepartment_IdIn(listRole,listDepartment,pageable);
    }

    @Override
    public Page<UserSummary> advancedSearch(String email, String displayName, String fullName, Integer departmentId,
                                              LocalDate birthDateFrom, LocalDate birthDateTo, Boolean gender, Pageable pageable) {
        email = email.toLowerCase();
        displayName = displayName.toLowerCase();
        fullName = fullName.toLowerCase();
        return userRepository.advancedSearch
                (email, displayName, fullName, departmentId, birthDateFrom,
                birthDateTo, gender, pageable);
    }

}
