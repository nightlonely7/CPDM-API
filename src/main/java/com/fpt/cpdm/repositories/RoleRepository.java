package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByName(String name);

    boolean existsByName(String name);
}
