package com.fpt.cpdm.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.cpdm.validators.users.EmailNotDuplicate;
import com.fpt.cpdm.validators.users.RoleMustMatch;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class User {

    private Long id;

    @NotNull
    @Size(min = 4, max = 30)
    private String displayName;

    @Size(min = 8, max = 20)
    private String password;

    @Email
    private String email;

    @Pattern(regexp = "^0(\\d{9})$")
    private String phone;

    @Size(max = 50)
    private String address;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthDay;

    @NotNull
    @RoleMustMatch
    private String role;
}
