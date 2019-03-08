-- INSERT ROLE
INSERT INTO role (name) VALUES
('ROLE_STAFF'), ('ROLE_MANAGER');

-- INSERT DEPARTMENT
INSERT INTO department (name, alias) VALUES
('New Department 1', 'NEW1'),
('New Department 2', 'NEW2');

-- INSERT USER
INSERT INTO user (display_name, full_name, password, email, phone, address, birthday, department_id, role_id, is_enabled) VALUES
('khanhnp', 'Nguyễn Phú Khánh', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'khanhnp@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-01-30', 1, 2, true),
('quang', 'Hoàng Vinh Quang', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u', 'quang@gmail.com'
, '0987654321', 'xyzabc tphcm', '1993-01-30', 1, 1, true),
('hoang', 'Nguyễn Phú Hoàng', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u','hoang@gmail.com'
, '0123456789', 'abcxyz tphcm', '1993-09-30', 2, 1, true);

-- INSERT DOCUMENT
INSERT INTO document (title, name_company, day_arrived, summary, link) VALUES
('abc', 'cty abc', '2016-05-22 18:30:31', '.......', 'abc.xyz'),
('cong van', 'cty aaa', '2017-09-20 07:22:11', '.......', 'abc.xyz'),
('tong tien', 'cty zzz', '2018-01-10 10:15:17', '.......', 'abc.xyz'),
('thong bao', 'cty abc', '2019-12-22 15:22:30', '.......', 'abc.xyz');


-- INSERT TASK
INSERT INTO task (title, summary, description, created_time,
start_time, end_time, priority, status, creator_id, executor_id, is_available) VALUES
('title task 01 aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', 'summary task 01', 'description task 01', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 02', 'summary task 02', 'description task 02', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 03', 'summary task 03', 'description task 03', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 04', 'summary task 04', 'description task 04', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 05', 'summary task 05', 'description task 05', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 06', 'summary task 06', 'description task 06', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 07', 'summary task 07', 'description task 07', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 08', 'summary task 08', 'description task 08', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 09', 'summary task 09', 'description task 09', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true),
('title task 10', 'summary task 10', 'description task 10', '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 1, 2, true);

-- INSERT TASK-DOCUMENT MAPPING
INSERT INTO tasks_documents(task_id, document_id) VALUES
(1, 1), (1, 2), (1, 3);

-- INSERT COMMENT
INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id) VALUES
('Test content','2019-02-28 23:59:59', '2019-02-28 23:59:59', 1, 1 , 1);