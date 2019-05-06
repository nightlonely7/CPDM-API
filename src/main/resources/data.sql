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
('Hướng dẫn về hợp đồng tư vấn xây dựng',
 'Hướng dẫn một số nội dung của các hợp đồng tư vấn xây dựng.',
'Thông tư này hướng dẫn một số nội dung của các hợp đồng tư vấn xây dựng gồm: tư vấn khảo sát xây dựng, tư vấn lập' ||
' Báo cáo nghiên cứu khả thi đầu tư xây dựng, tư vấn thiết kế xây dựng công trình, tư vấn giám sát thi công xây ' ||
'dựng công trình (sau đây gọi chung là tư vấn xây dựng) thuộc các dự án đầu tư xây dựng ' ||
'(bao gồm cả hợp đồng xây dựng giữa nhà đầu tư thực hiện dự án đối tác công tư PPP với nhà ' ||
'thầu thực hiện các gói thầu của dự án)',
'2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Quy định về phân cấp công trình xây dựng',
'Quy định về phân cấp công trình xây dựng và hướng dẫn áp dụng trong quản lý hoạt động đầu tư xây dựng',
 'Thông tư này quy định chi Tiết về phân cấp công trình xây dựng và hướng dẫn áp dụng cấp công trình xây ' ||
  'dựng trong quản lý các hoạt động ' ||
  'đầu tư xây dựng công trình theo quy định tại Khoản 3 Điều 8 Nghị định 46/2015/NĐ-CP.',
  '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Hướng dẫn về cấp giấy phép xây dựng',
 'Thông tư này quy định chi tiết về hồ sơ đề nghị cấp giấy phép xây dựng và điều chỉnh giấy phép xây dựng',
  'Thông tư này quy định chi tiết về hồ sơ đề nghị cấp giấy phép xây dựng, Điều chỉnh giấy phép xây dựng, gia hạn giấy' ||
   ' phép xây dựng; giấy phép xây dựng có thời hạn; quy trình và thẩm quyền cấp giấy phép xây dựng.',
   '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Hướng dẫn về năng lực của tổ chức, cá nhân',
 'Hướng dẫn về năng lực hoạt động xây dựng',
  'Phạm vi Điều chỉnh: Thông tư này hướng dẫn về năng lực hoạt động xây dựng, gồm: năng lực hành nghề hoạt động xây' ||
   ' dựng của cá nhân; năng lực hoạt động xây dựng của tổ chức; công bố thông tin về năng lực hoạt động xây dựng.',
   '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Hướng dẫn hợp đồng thiết kế - cung cấp thiết bị công nghệ ',
'Hướng dẫn hợp đồng thiết kế - cung cấp thiết bị công nghệ và thi công xây dựng công trình',
 'Thông tư này hướng dẫn về hợp đồng thiết kế - cung cấp thiết bị công nghệ và thi công xây dựng công trình thuộc các ' ||
  'dự án đầu tư xây dựng' ||
  'dự án đầu tư xây dựng theo quy định tại khoản 2 Điều 1 Nghị định số 37/2015/NĐ-CP ngày 22/4/2015 của Chính phủ' ||
   ' quy định chi tiết về hợp đồng xây dựng',
  '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Hướng dẫn xác định và quản lý chi phí khảo sát xây dựng',
 'Hướng dẫn xác định và quản lý chi phí khảo sát xây dựng phục vụ lập dự án đầu tư xây dựng, thiết kế',
  'Thông tư này hướng dẫn việc xác định và quản lý chi phí khảo sát xây dựng phục vụ lập dự án đầu tư xây dựng, thiết kế' ||
   ' xây dựng công trình, khảo sát phục vụ lập quy hoạch xây dựng và các công tác khảo sát khác có liên quan' ||
    ' trong hoạt động đầu tư xây dựng.',
   '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Quy định mức thu, chế độ thu, nộp, quản lý',
 'Quy định mức thu, chế độ thu, nộp, quản lý và sử dụng phí thẩm định dự án đầu tư xây dựng',
  'Thông tư này quy định mức thu, chế độ thu, nộp, quản lý và sử dụng phí thẩm định dự án đầu tư xây dựng,' ||
   ' phí thẩm định thiết kế cơ sở.',
   '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('hướng dẫn xác định đơn giá nhân công trong quản lý chi phí đầu tư xây dựng',
 'Hướng dẫn xác định đơn giá nhân công để quản lý chi phí đầu tư xây dựng công trình .',
  'Hướng dẫn xác định đơn giá nhân công để quản lý chi phí đầu tư xây dựng công trình bao gồm: tổng mức' ||
   ' đầu tư, dự toán xây dựng, dự toán gói thầu, giá xây dựng, giá hợp đồng, chỉ số giá xây dựng.' ||
    'Khuyến khích các cơ quan, tổ chức, cá nhân có liên quan đến việc quản lý chi phí đầu tư xây dựng sử dụng' ||
     ' các nguồn vốn khác áp dụng các quy định của Thông tư này.',
   '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Công bố định mức các hao phí xác định giá ca máy và thiết bị thi công xây dựng',
 'Công bố định mức các hao phí xác định giá ca máy và thiết bị thi công xây dựng kèm theo quyết định của nhà nước',
  'Công bố định mức các hao phí xác định giá ca máy và thiết bị thi công xây dựng kèm theo quyết định này để các cơ quan,'' ||
  '' tổ chức, các nhân có liên quan tham khảo, sử dụng trong xác định và quản lý chi phí đầu tư xây dựng.' ||
   'Định mức các hao phí xác định giá ca máy gồm: số ca làm việc trong năm; định mức khấu hao, sửa chữa, tiêu hao ' ||
   'nhiên liệu - năng lượng, nhân công điều khiển và định mức chi phí khác.', '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true),
('Công bố định mức chi phí quản lý dự án và tư vấn đầu tư xây dựng',
 'Công bố định mức chi phí quản lý dự án và tư vấn đầu tư xây dựng kèm theo quyết định của nhà nước',
  'Công bố định mức chi phí quản lý dự án và tư vấn đầu tư xây dựng kèm theo quyết định này để các cơ quan, tổ chức, ' ||
   'cá nhân có liên quan xác định chi phí quản lý dự án và tư vấn đầu tư xây dựng trong tổng mức đầu tư xây dựng,' ||
    ' dự toán xây dựng công trình, xác định giá gói thầu và quản lý chi phí đầu tư xây dựng các dự án thuộc đối' ||
     ' tượng áp dụng của Nghị định số 32/2015/NĐ-CP ngày 25/3/2015 của Chính phủ về quản lý chi phí đầu tư xây dựng.',
   '2016-05-22 18:30:31', '2016-01-22 18:30:31', '2016-06-22 18:30:31', 1, 1, true);

-- INSERT TASK
INSERT INTO task (title, summary, description, created_time,
start_time, end_time, priority, status, creator_id, executor_id, project_id, parent_task_id, available) VALUES
('Đấu thầu dự án Hải Phòng',
 'Đấu thầu dự án Hải Phòng và báo cáo tiến độ dự án.',
 'Đấu thầu dự án Hải Phòng vào và báo cáo tiến độ dự án sau khi đấu thầu kết thúc. ' ||
  'Lấy đơn báo giá về tiền vật liệu và xây dựng.',
  '2019-01-28 16:30:00', '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo tiến độ dự án Đà Lạt',
 'Báo cáo tiến độ dự án Đà Lạt và tính toán giá cả dự án',
  'Báo cáo tiến độ dự án Đà Lạt và tính toán giá cả dự án. Lấy đơn báo giá về tiền vật liệu và xây dựng.',
  '2019-01-28 16:30:00', '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo giá cả dự án TPHCM',
'Báo cáo giá cả dự án TPHCM và tính toán tiền bảo trì máy móc',
'Báo cáo giá cả dự án TPHCM và tính toán tiền bảo trì máy móc. ' ||
 'Lấy đơn báo giá về tiền bảo trì và chi phí phát sinh.',
'2019-01-28 16:30:00', '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo giá cả dự án Đà Nẵng',
'Báo cáo giá cả dự án Đà Nẵng và lấy đơn báo giá từng phần của dự án',
'Báo cáo giá cả dự án Đà Nẵng và lấy đơn báo giá từng phần của dự án. ' ||
 'Thông báo cho văn phòng kế toán về đơn báo giá.',
'2019-01-28 16:30:00', '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo giá cả vật liệu xây dựng dự án TPHCM',
 'Báo cáo giá cả vật liệu xây dựng dự án TPHCM và tính toán chi phí phát sinh.',
  'Báo cáo giá cả vật liệu xây dựng dự án TPHCM và tính toán chi phí phát sinh. ' ||
   'Thông báo cho văn phòng kế toán về giá cả phát sinh.',
  '2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo tiền lương nhân viên tháng 10',
'Báo cáo tiền lương nhân viên tháng 10. Báo cáo tống chi phí tiền lương toàn bộ tháng 10.',
'Báo cáo tiền lương nhân viên tháng 10. Báo cáo tống chi phí tiền lương toàn bộ tháng 10. ' ||
 'Thông báo sai sót trong chi phí.',
'2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo giá cả bảo trì máy móc tháng 7',
'Báo cáo giá cả bảo trì máy móc tháng 7 và chi phí phát sinh.',
'Báo cáo giá cả bảo trì máy móc tháng 7 và chi phí phát sinh. Thông báo tống giá lên văn phòng kế toán.',
'2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo tiến độ bảo trì hệ thống tháng 12',
'Báo cáo tiến độ bảo trì hệ thống tháng 12 và chi phí bảo trì cộng với trang thái hệ thống.',
'Báo cáo tiến độ bảo trì hệ thống tháng 12 và chi phí bảo trì cộng với trang thái hệ thống. ' ||
 'Báo cáo tộng tiền lên quản lí của văn phòng kế toán.',
'2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Đấu thầu dự án Bình Dương',
'Đấu thầu dự án Bình Dương và chi phí dự án.',
'Đấu thầu dự án Bình Dương và chi phí dự án. Thông báo chi phí phát sinh và tông chi phí của cả dự án ' ||
 'đi kèm với chi phí từng phần của dự án.',
'2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true),
('Báo cáo số lượng vật liệu cho dự án Bình Dương',
'Báo cáo số lượng vật liệu cho dự án Bình Dương và chi phí toàn bộ vật liệu.',
'Báo cáo số lượng vật liệu cho dự án Bình Dương và chi phí toàn bộ vật liệu. ' ||
 'Tính toán các chi phí phát sinh và thông báo toàn bộ giá lên văn phòng kế toán.',
'2019-01-28 16:30:00'
, '2019-01-28 16:30:00', '2019-02-28 23:59:59', 1, 'Working', 9, 1, 1, null, true);
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
('Đơn xin nghỉ phép 1 ngày', 1,'2019-03-15', '2019-04-15', '2019-04-14', 1, 2, 1),
('Đơn xin nghỉ phép 1 ngày', 1,'2019-03-08', '2019-04-08', '2019-04-07', 1, 2, 1),
('Đơn xin nghỉ phép 1 ngày', 1,'2019-03-01', '2019-04-01', '2019-03-28', 1, 2, 1);
