insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('thdddnjstjr',1234,'고죠','고죠','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test',1234,'가을','정훈','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test1',1234,'겨울','짱구','010-1234-5678','male','27');

-- 임시 데이터 학교
INSERT INTO school_tb (id, school, name, region) VALUES
(1, 'ABC School', 'ABC', 'Seoul'),
(2, 'XYZ School', 'XYZ', 'Busan');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC4yMjkyMzc5MTMyMDA3', 1000, 1000, '2024-10-21 12:50:31', '휴대폰', 'tviva20241021124957fLyI0', 'none');


insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC4yNTM2NTY3NjQzODc3', 1000, 1000, '2024-10-25 18:21:23', '휴대폰', 'tviva20241025182058fESt7', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC4yNjcxMTU1ODY1NTIw', 1000, 1000, '2024-10-25 18:21:23', '휴대폰', 'tviva20241025182937rTWv7', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC4xNzQ5NzM4MTQwMDE0', 1000, 1000, '2024-10-28 18:21:23', '휴대폰', 'tviva20241028093632rKXj1', 'none');

        insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC4wMzY5MTA2MDk5MTIy', 1000, 1000, '2024-10-28 14:39:01', '휴대폰', 'tviva20241028143745iHv15', 'none');

        insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC40MTk1MzczMzQzMDI5', 1000, 1000, '2024-10-28 14:39:01', '간편결제', 'tviva20241028164844i11C4', 'none');


