package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {

}
