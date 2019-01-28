package com.fpt.cpdm.models;

import com.fpt.cpdm.validators.users.RoleMustMatch;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @EqualsAndHashCode.Include
    private Integer id;

    @RoleMustMatch
    private String name;
}
