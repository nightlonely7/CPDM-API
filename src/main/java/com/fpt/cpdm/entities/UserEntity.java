package com.fpt.cpdm.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Entity(name = "User")
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class UserEntity extends BaseEntity implements UserDetails {

    public UserEntity(Integer id) {
        super.setId(id);
    }

    @Basic
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Basic
    @Column(name = "display_name", nullable = false, length = 30)
    private String displayName;

    @Basic
    @Column(name = "full_name", length = 50)
    private String fullName;

    @Basic
    @Column(name = "gender")
    private Boolean gender;

    @Basic
    @Column(name = "password", nullable = false)
    private String password;

    @Basic
    @Column(name = "phone", length = 10)
    private String phone;

    @Basic
    @Column(name = "address", length = 50)
    private String address;

    @Basic
    @Column(name = "birthday")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @Basic
    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToMany(mappedBy = "relatives")
    private List<TaskEntity> relatedTasks;

    @Basic
    @Column(name = "is_enabled")
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.getName()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @PrePersist
    public void prePersist() {
        this.enabled = true;
        this.createdTime = LocalDateTime.now();
    }
}
