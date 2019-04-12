package com.fpt.cpdm.utils;


import com.fpt.cpdm.entities.*;
import com.fpt.cpdm.models.Role;
import com.fpt.cpdm.models.askingRequests.AskingRequest;
import com.fpt.cpdm.models.assignRequests.AssignRequest;
import com.fpt.cpdm.models.comments.Comment;
import com.fpt.cpdm.models.leaveRequests.LeaveRequest;
import com.fpt.cpdm.models.projects.Project;
import com.fpt.cpdm.models.storedComments.StoredComment;
import com.fpt.cpdm.models.departments.Department;
import com.fpt.cpdm.models.documents.Document;
import com.fpt.cpdm.models.tasks.Task;
import com.fpt.cpdm.models.users.User;
import org.modelmapper.ModelMapper;


public class ModelConverter {

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();

    public static Role roleEntityToModel(RoleEntity roleEntity) {
        Role role = MODEL_MAPPER.map(roleEntity, Role.class);
        return role;
    }

    public static RoleEntity roleModelToEntity(Role role) {
        RoleEntity roleEntity = MODEL_MAPPER.map(role, RoleEntity.class);
        return roleEntity;
    }

    public static User userEntityToModel(UserEntity userEntity) {
        User user = MODEL_MAPPER.map(userEntity, User.class);
        user.getRole().setName(user.getRole().getName().substring(5));
        user.setPassword(null);
        return user;
    }

    public static UserEntity userModelToEntity(User user) {
        UserEntity userEntity = MODEL_MAPPER.map(user, UserEntity.class);
        return userEntity;
    }

    public static Task taskEntityToModel(TaskEntity taskEntity) {
        Task task = MODEL_MAPPER.map(taskEntity, Task.class);
        return task;
    }

    public static TaskEntity taskModelToEntity(Task task) {
        TaskEntity taskEntity = MODEL_MAPPER.map(task, TaskEntity.class);
        return taskEntity;
    }

    public static Document documentEntityToModel(DocumentEntity documentEntity) {
        Document document = MODEL_MAPPER.map(documentEntity, Document.class);
        return document;
    }

    public static DocumentEntity documentModelToEntity(Document document) {
        DocumentEntity documentEntity = MODEL_MAPPER.map(document, DocumentEntity.class);
        return documentEntity;
    }

    public static Comment commentEntityToModel(CommentEntity commentEntity) {
        Comment comment = MODEL_MAPPER.map(commentEntity, Comment.class);
        return comment;
    }

    public static CommentEntity commentModelToEntity(Comment comment) {
        CommentEntity commentEntity = MODEL_MAPPER.map(comment, CommentEntity.class);
        return commentEntity;
    }

    public static StoredComment storedCommentEntityToModel(StoredCommentEntity storedCommentEntity) {
        StoredComment storedComment = MODEL_MAPPER.map(storedCommentEntity, StoredComment.class);
        return storedComment;
    }

    public static StoredCommentEntity storedCommentModelToEntity(StoredComment storedComment) {
        StoredCommentEntity storedCommentEntity = MODEL_MAPPER.map(storedComment, StoredCommentEntity.class);
        return storedCommentEntity;
    }

    public static Department departmentEntityToModel(DepartmentEntity departmentEntity) {
        Department department = MODEL_MAPPER.map(departmentEntity, Department.class);
        return department;
    }

    public static DepartmentEntity departmentModelToEntity(Department department) {
        DepartmentEntity departmentEntity = MODEL_MAPPER.map(department, DepartmentEntity.class);
        return departmentEntity;
    }

    public static Project projectEntityToModel(ProjectEntity projectEntity) {
        Project project = MODEL_MAPPER.map(projectEntity, Project.class);
        return project;
    }

    public static ProjectEntity projectModelToEntity(Project project) {
        ProjectEntity projectEntity = MODEL_MAPPER.map(project, ProjectEntity.class);
        return projectEntity;
    }

    public static LeaveRequest leaveRequestEntityToModel(LeaveRequestEntity leaveRequestEntity) {
        LeaveRequest leaveRequest = MODEL_MAPPER.map(leaveRequestEntity, LeaveRequest.class);
        return leaveRequest;
    }

    public static LeaveRequestEntity leaveRequestModelToEntity(LeaveRequest leaveRequest) {
        LeaveRequestEntity leaveRequestEntity = MODEL_MAPPER.map(leaveRequest, LeaveRequestEntity.class);
        return leaveRequestEntity;
    }

    public static AssignRequest assignRequestEntityToModel(AssignRequestEntity assignRequestEntity) {
        AssignRequest assignRequest = MODEL_MAPPER.map(assignRequestEntity, AssignRequest.class);
        return assignRequest;
    }

    public static AssignRequestEntity assignRequestModelToEntity(AssignRequest assignRequest) {
        AssignRequestEntity assignRequestEntity = MODEL_MAPPER.map(assignRequest, AssignRequestEntity.class);
        return assignRequestEntity;
    }
    public static AskingRequest askingRequestEntityToModel(AskingRequestEntity askingRequestEntity) {
        AskingRequest askingRequest = MODEL_MAPPER.map(askingRequestEntity, AskingRequest.class);
        return askingRequest;
    }

    public static AskingRequestEntity askingRequestModelToEntity(AskingRequest askingRequest) {
        AskingRequestEntity askingRequestEntity = MODEL_MAPPER.map(askingRequest, AskingRequestEntity.class);
        return askingRequestEntity;
    }
}
