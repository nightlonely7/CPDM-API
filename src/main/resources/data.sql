-- INSERT ROLE
INSERT INTO role (name,display_name) VALUES
('ROLE_STAFF','Nhân viên'),
('ROLE_MANAGER','Quản lý'),
('ROLE_ADMIN','Giám đốc');

-- INSERT DEPARTMENT
INSERT INTO department (name, alias, description, available, created_time, last_modified_time) VALUES
('Văn Phòng Quản Li Xây Dựng', 'QLXD', 'Văn Phòng Quản Li Xây Dựng', true, '2017-09-20 07:22:11', '2017-09-20 07:22:11'),
('Văn Phòng Quản Li Ngân Sách', 'QLNS', 'Văn Phòng Quản Li Ngân Sách', true, '2017-09-20 07:22:11', '2017-09-20 07:22:11'),
('Văn Phòng Quản Li Nhân Viên', 'QLNV', 'Văn Phòng Quản Li Nhân Viên', true, '2017-09-20 07:22:11', '2017-09-20 07:22:11'),
('Văn Phòng Nhân Sự', 'NS', 'Văn Phòng Nhân Sự', true, '2017-09-20 07:22:11', '2017-09-20 07:22:11');

--INSERT PROJECT
INSERT INTO project(name, alias, description, available, created_time, last_modified_time) values
('Dự án thầu Hải Phòng 2019', 'DAT 2019', 'Dự án thầu Hải Phòng 2019', true, '2017-09-20 07:22:11', '2019-07-23 07:22:11'),
('Dự án nhà nước Bình Dương 2018', 'DANN 2018', 'Dự án nhà nước Bình Dương 2018', true, '2017-09-30 07:22:11', '2018-08-24 07:22:11'),
('Dự án thầu TPHCM 2017', 'DAT 2017', 'Dự án thầu TPHCM 2017', true, '2016-04-20 07:22:11', '2017-09-10 07:22:11'),
('Dự án tư nhân TPHCM 2019', 'DATU 2018', 'Dự án tư nhân TPHCM 2019', true, '2015-02-27 07:22:11', '2019-06-15 07:22:11');

-- INSERT USER
INSERT INTO user (display_name, full_name, gender, password, email, phone, address, birthday, department_id, role_id, is_enabled, created_time) VALUES
('khanhnp', 'Nguyễn Phú Khánh', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'khanhnp@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 2, true, '2017-09-20 07:22:11'),
('quang', 'Hoàng Vinh Quang', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'quang@gmail.com'
, '0987654321', 'xyzabc tphcm', '1993-01-30', 1, 1, true, '2017-09-20 07:22:11'),
('hoang', 'Nguyễn Hoàng', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u','hoang@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-09-30', 2, 1, true, '2017-09-20 07:22:11'),
('nam', 'Lưu Thành Nam', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'luutn@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 1, true, '2017-09-20 07:22:11'),
('dao', 'Lê Thị Đào', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'daolt@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 1, true, '2017-09-20 07:22:11'),
('truong', 'Đàm Xuân Trường', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'truongdx@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 1, true, '2017-09-20 07:22:11'),
('han', 'Văn Hân', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'hanv@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 1, true, '2017-09-20 07:22:11'),
('minh', 'Nguyễn Hoàng Minh', false, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'minhnh@gmail.com'
, '0123456789', 'abcxyz tphcm', '2000-01-30', 1, 1, true, '2017-09-20 07:22:11'),
('admin', 'Admin', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'admin@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 3, true, '2017-09-20 07:22:11'),
('hoangcm', 'Cao Minh Hoàng', true, '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'hoangcm@gmail.com'
, '0907419552', 'abcxyz tphcm', '1993-01-30', 2, 2, true, '2017-09-20 07:22:11');

-- INSERT DOCUMENT
INSERT INTO document (title, summary, description, created_time, start_time, end_time, creator_id, project_id, available) VALUES
('document summary 1', 'document summary 1', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 2', 'document summary 2', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 3', 'document summary 3', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 4', 'document summary 4', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 5', 'document summary 5', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 6', 'document summary 6', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 7', 'document summary 7', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 8', 'document summary 8', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 9', 'document summary 9', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('document title 10', 'document summary 10', 'description', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true);

-- INSERT TASK
INSERT INTO task (title, summary, description, created_time,
start_time, end_time, priority, status, creator_id, executor_id, project_id, parent_task_id, available) VALUES
('Tác vụ số 1', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 2', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 3', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 4', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 5', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 6', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 7', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 8', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 9', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Tác vụ số 10', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('title task 02', 'summary task 02', 'description task 02', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-03-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 03', 'summary task 03', 'description task 03', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-04-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 04', 'summary task 04', 'description task 04', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-04-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 05', 'summary task 05', 'description task 05', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-03-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 06', 'summary task 06', 'description task 06', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-04-08 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 07', 'summary task 07', 'description task 07', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 08', 'summary task 08', 'description task 08', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 09', 'summary task 09', 'description task 09', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 10', 'summary task 10', 'description task 10', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
('title task 11', 'summary task 11', 'description task 11', '2019-03-28 16:30:00'
, '2019-05-27 16:30:00', '2019-05-29 23:59:59', 1, 'Waiting', 1, 2, 1, 1, true);
-- ('title task 11b', 'summary task 11b', 'description task 11b', '2019-03-28 16:30:00'
-- , '2019-03-29 16:30:00', '2019-03-30 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
-- ('title task 11c', 'summary task 11c', 'description task 11c', '2019-03-28 16:30:00'
-- , '2019-03-29 16:30:00', '2019-03-31 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
-- ('title task 11d', 'summary task 11d', 'description task 11d', '2019-03-28 16:30:00'
-- , '2019-03-30 16:30:00', '2019-04-02 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
-- ('title task 12', 'summary task 12', 'description task 12', '2019-03-29 07:30:00'
-- , '2019-03-29 07:30:00', '2019-03-29 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
-- ('title task 12b', 'summary task 12b', 'description task 12b', '2019-03-29 07:30:00'
-- , '2019-03-29 07:30:00', '2019-03-29 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
-- ('title task 12c', 'summary task 12c', 'description task 12c', '2019-03-29 07:30:00'
-- , '2019-03-29 07:30:00', '2019-03-29 23:59:59', 1, 'Working', 1, 2, 1, 1, true),
-- ('title task 12d', 'summary task 12d', 'description task 12d', '2019-03-29 07:30:00'
-- , '2019-03-29 07:30:00', '2019-03-29 23:59:59', 1, 'Working', 1, 2, 1, 1, true);

-- INSERT DOCUMENTS-RELATIVES MAPPING
INSERT INTO documents_relatives(document_id, user_id) VALUES
(1, 3), (1, 4), (1, 5), (1, 6),
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1), (7, 1), (8, 1), (9, 1), (10, 1);

-- INSERT TASKS-RELATIVES MAPPING
INSERT INTO tasks_relatives(task_id, user_id) VALUES
(1, 3), (1, 4), (1, 5), (1, 6);

-- INSERT TASK_ISSUE
INSERT INTO task_issue(task_id, summary, description, completed, creator_id, created_time, last_editor_id, last_modified_time, available) VALUES
(1, 'issue 1', 'detail 1', false, 1, '2019-02-28 23:59:59', 1, '2019-02-28 23:59:59', true),
(1, 'issue 2', 'detail 2', false, 1, '2019-02-28 23:59:59', 1, '2019-02-28 23:59:59', true),
(1, 'issue 3', 'detail 3', false, 1, '2019-02-28 23:59:59', 1, '2019-02-28 23:59:59', true),
(1, 'issue 4', 'detail 4', false, 1, '2019-02-28 23:59:59', 1, '2019-02-28 23:59:59', true),
(1, 'issue 5', 'detail 5', false, 1, '2019-02-28 23:59:59', 1, '2019-02-28 23:59:59', true);

-- INSERT TASK-DOCUMENT MAPPING
INSERT INTO tasks_documents(task_id, document_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5);

-- INSERT COMMENT
INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id,parent_comment_id) VALUES
('Test content','2019-02-28 23:59:59', '2019-02-28 23:59:59', 0, 1 , 1, null);
INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id,parent_comment_id) VALUES
('Cuộc họp diễn lúc 7 giờ','2019-02-28 12:59:59', '2019-02-28 23:59:59', 0, 1 , 7, null);
INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id,parent_comment_id) VALUES
('Hẹn gặp lại','2019-02-28 13:59:59', '2019-02-28 23:59:59', 0, 2 , 7, null);

INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id,parent_comment_id) VALUES
('Tôi sẽ đến đúng giờ','2019-02-28 14:59:59', '2019-02-28 23:59:59', 0, 2 , 7, null);
INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id,parent_comment_id) VALUES
('Cám ơn đã thông báo','2019-02-28 15:59:59', '2019-02-28 23:59:59', 1, 2 , 7, null);
INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id,parent_comment_id) VALUES
('Da bi xoa','2019-02-28 16:59:59', '2019-02-28 23:59:59', 2, 2 , 7, null);

-- INSERT STORED COMMENT
INSERT INTO stored_comment (content, created_date, comment_id) VALUES
('Cám ơn đã thông bá','2019-02-28 15:29:59', 5);
INSERT INTO stored_comment (content, created_date, comment_id) VALUES
('Cám ơn đã thông báx','2019-02-28 15:30:59', 5);

-- INSERT LEAVE REQUEST

INSERT INTO leave_request (content, day_off, from_date, to_date, created_date, status, user_id, approver_id) VALUES
('Đơn xin nghỉ phép 2 ngày', 2,'2019-03-17', '2019-04-18', '2019-04-16', 0, 2, 1),
('Đơn xin nghỉ phép 1 ngày', 1,'2019-03-15', '2019-04-15', '2019-04-14', 1, 2, 1),
('Đơn xin nghỉ phép 3 ngày', 3,'2019-03-11', '2019-04-13', '2019-04-10', 0, 2, 1),
('Đơn xin nghỉ phép 1 ngày', 1,'2019-03-08', '2019-04-08', '2019-04-07', 1, 2, 1),
('Đơn xin nghỉ phép 2 ngày', 2,'2019-03-06', '2019-04-07', '2019-04-05', 0, 2, 1),
('Đơn xin nghỉ phép 1 ngày', 1,'2019-03-01', '2019-04-01', '2019-03-28', 1, 2, 1);
