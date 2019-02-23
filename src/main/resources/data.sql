INSERT INTO role (name) VALUES ('ROLE_STAFF');
INSERT INTO role (name) VALUES ('ROLE_MANAGER');

INSERT INTO department (name, alias) VALUES ('New Department 1', 'NEW1');

INSERT INTO user (display_name, password, email, phone, address, department_id, role_id)
            values ('khanhnp', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u',
             'khanhnp@gmail.com', '0123456789', 'abcxyz tphcm', 1, 2);
INSERT INTO user (display_name, password, email, phone, address, department_id, role_id)
            values ('quang', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u',
             'quang@gmail.com', '0987654321', 'xyzabc tphcm', 1, 1);

INSERT INTO document (title, name_company, day_arrived, summary, link) VALUES ('abc', 'cty abc', '2016/05/22 18:30:31', '.......', 'abc.xyz');
INSERT INTO document (title, name_company, day_arrived, summary, link) VALUES ('cong van', 'cty aaa', '2017/09/20 07:22:11', '.......', 'abc.xyz');
INSERT INTO document (title, name_company, day_arrived, summary, link) VALUES ('tong tien', 'cty zzz', '2018/01/10 10:15:17', '.......', 'abc.xyz');
INSERT INTO document (title, name_company, day_arrived, summary, link) VALUES ('thong bao', 'cty abc', '2019/12/22 15:22:30', '.......', 'abc.xyz');


INSERT INTO task (title, summary, description, created_time, start_time, end_time, priority, status, creator_id, executor_id)
          VALUES ('Day la title task 01', 'Day la summary task 01', 'Day la description task 01', '2019-01-28 16:30:00', '2019-01-28 16:30:00',
           '2019-02-28 23:59:59', 1, 'Working', 1, 2);

INSERT INTO documents_tasks( task_id, document_id) values (1, 1);
INSERT INTO documents_tasks( task_id, document_id) values (1, 2);
INSERT INTO documents_tasks( task_id, document_id) values (1, 3);

INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id)
  VALUES ('Test content','2019-02-28 23:59:59', '2019-02-28 23:59:59', 1, 1 , 1);