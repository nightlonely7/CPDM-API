package com.fpt.cpdm.utils;

public class Enum {
    public enum CommentStatus{
        New (0), Edited (1), Deleted (2);

        private final int commentStatusCode;

        private CommentStatus(int commentStatusCode) {
            this.commentStatusCode = commentStatusCode;
        }

        public int getCommentStatusCode() {
            return this.commentStatusCode;
        }
    }

    public enum LeaveRequestStatus{
        New (0), Approved (1), Declined (2);

        private final int leaveRequestStatusCode;

        private LeaveRequestStatus(int leaveRequestStatusCode) {
            this.leaveRequestStatusCode = leaveRequestStatusCode;
        }

        public int getLeaveRequestStatusCode() {
            return this.leaveRequestStatusCode;
        }
    }

    public enum AssignRequestStatus{
        New (0), Approved (1), Declined (2);

        private final int assignRequestStatusCode;

        private AssignRequestStatus(int assignRequestStatusCode) {
            this.assignRequestStatusCode = assignRequestStatusCode;
        }

        public int getAssignRequestStatusCode() {
            return this.assignRequestStatusCode;
        }
    }
    public enum AskingRequestStatus{
        Waiting (0), Replied (1);

        private final int askingRequestStatusCode;

        private AskingRequestStatus(int askingRequestStatusCode) {
            this.askingRequestStatusCode = askingRequestStatusCode;
        }

        public int getAskingRequestStatusCode() {
            return this.askingRequestStatusCode;
        }
    }
}
