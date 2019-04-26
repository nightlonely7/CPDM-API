package com.fpt.cpdm.repositories;

import com.fpt.cpdm.entities.TaskHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistoryEntity, Integer> {

}
