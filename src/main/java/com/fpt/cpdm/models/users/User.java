package com.fpt.cpdm.models.users;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.departments.Department;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private Integer id;

    @Size(min = 4, max = 30)
    private String displayName;

    @Size(min = 4, max= 50)
    private String fullName;

    private Boolean gender;

    @Size(min = 8, max = 20)
    private String password;

    @NotNull
    @Email
    private String email;

    @Pattern(regexp = "^0(\\d{9})$")
    private String phone;

    @Size(max = 50)
    private String address;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    private Department department;

    private Role role;

    @NotNull
    private boolean isEnabled;
}
