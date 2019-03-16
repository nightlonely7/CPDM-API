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
        New (0), Approved (1), OverDate (2);

        private final int leaveRequestStatusCode;

        private LeaveRequestStatus(int leaveRequestStatusCode) {
            this.leaveRequestStatusCode = leaveRequestStatusCode;
        }

        public int getLeaveRequestStatusCode() {
            return this.leaveRequestStatusCode;
        }
    }
}
