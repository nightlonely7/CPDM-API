package com.fpt.cpdm.services;

import com.fpt.cpdm.models.IdOnlyForm;
import com.fpt.cpdm.models.users.UserSummary;

import java.util.List;

public interface TaskRelativeService {

    List<UserSummary> readAll(Integer taskId);

    List<UserSummary> add(Integer taskId, List<IdOnlyForm> taskIssueForm);

    void delete(Integer id);
}
