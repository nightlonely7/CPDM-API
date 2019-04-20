package com.fpt.cpdm.services;

import com.fpt.cpdm.forms.tasks.TaskCreateForm;
import com.fpt.cpdm.forms.tasks.TaskSearchForm;
import com.fpt.cpdm.forms.tasks.TaskUpdateForm;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.tasks.TaskBasic;
import com.fpt.cpdm.models.tasks.TaskDetail;
import com.fpt.cpdm.models.tasks.TaskSummary;
import com.fpt.cpdm.models.tasks.task_issues.TaskIssueStatus;
import com.fpt.cpdm.models.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    void uploadFile(Integer id, String filename);

    TaskDetail findDetailById(Integer id);

    TaskIssueStatus findIssueStatusById(Integer id);

    TaskSummary complete(Integer id);

    TaskDetail create(TaskCreateForm taskCreateForm);

    TaskDetail update(Integer id, TaskUpdateForm taskUpdateForm);

    Page<TaskSummary> findAllSummaryByExecutor(TaskSearchForm taskSearchForm, Pageable pageable);

    List<TaskSummary> findAllSummaryByExecutor();

    Page<TaskSummary> findAllSummaryByCreator(TaskSearchForm taskSearchForm, Pageable pageable);

    List<TaskSummary> findAllSummaryByCreator();

    Page<TaskSummary> findAllSummaryByRelatives(TaskSearchForm taskSearchForm, Pageable pageable);

    List<TaskSummary> findAllSummaryByRelatives();

    void deleteById(Integer id);

    Page<TaskSummary> findAllByParentTask_Id(Integer taskId, Pageable pageable);

    List<TaskSummary> findAllBasicByCurrentExecutorAndProject_Id(Integer projectId);

    boolean existsByExecutorAndStatusAndStartTimeLessThanEqualAndStartTimeGreaterThanEqual(User user, String status, LocalDateTime fromTime, LocalDateTime toTime);

    boolean existsByExecutorAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(User user, String status, LocalDateTime fromTime);

    List<TaskSummary> findAllByExecutorAndStatusAndStartTimeLessThanEqualAndStartTimeGreaterThanEqual(User user, String status, LocalDateTime fromTime, LocalDateTime toTime);

    List<TaskSummary> findAllByExecutorAndStatusAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(User user, String status, LocalDateTime fromTime);

    Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndNotAssigned(String status, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndFullAssigned(String status, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndPartAssigned(String status, LocalDate fromDate, LocalDate toDate, Pageable pageable);

    Page<TaskSummary> findAllSummaryByExecutorAndDateRangeAndStatus(String status, LocalDateTime fromTime, LocalDateTime toTime, Pageable pageable);
}
