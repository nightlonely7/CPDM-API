INSERT INTO role (name) VALUES ('ROLE_STAFF');
INSERT INTO role (name) VALUES ('ROLE_MANAGER');
INSERT INTO user (display_name, password, email, phone, address, role_id)
            values ('khanhnp', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u',
             'khanhnp@gmail.com', '0123456789', 'abcxyz tphcm', 1);
INSERT INTO user (display_name, password, email, phone, address, role_id)
            values ('quang', '$2a$11$JDbss487mfwgvAzx7g.6L.Y2hXwLh58861Q.wvLKIbfr0b9gDzh3u',
             'quang@gmail.com', '0987654321', 'xyzabc tphcm', 2);
INSERT INTO document (title, summary) VALUES ('abc', '123');
INSERT INTO document (title, summary) VALUES ('123', 'qwe');
INSERT INTO document (title, summary) VALUES ('qwe', 'asd');
INSERT INTO document (title, summary) VALUES ('asd', 'zxc');
INSERT INTO task (title, description, created_time, start_time, end_time, priority, status, creator_id, executor_id)
          VALUES ('Day la task 01', 'Day la task 01', '2019-01-28 16:30:00', '2019-01-28 16:30:00',
           '2019-02-28 23:59:59', 1, 'Working', 1, 2);
INSERT INTO documents_tasks( task_id, document_id) values (1, 1);
INSERT INTO documents_tasks( task_id, document_id) values (1, 2);
INSERT INTO documents_tasks( task_id, document_id) values (1, 3);

INSERT INTO comment (content, created_date, last_modified_date, status, user_id, task_id)
  VALUES ('Test content','2019-02-28 23:59:59', '2019-02-28 23:59:59', 1, 1 , 1);