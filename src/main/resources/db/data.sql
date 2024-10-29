insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('thddnjstjr','1234','고죠','고죠','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test',1234,'가을','정훈','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test1',1234,'겨울','짱구','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test2',1234,'봄','도라에몽','010-1234-5678','female','17');


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
---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트1', '테스트123123', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트2', '테스트124124', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트3', '테스트125125', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트4', '테스트126126', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트5', '테스트127127', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트6', '테스트128128', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트7', '테스트129129', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트8', '테스트120120', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테스트9', '테스트12121212', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테', '테스트127127', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '비가온다주륵주륵주륵주륵주륵', '주르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '오늘의 테스트', '얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테1', '테스트1271527', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테2', '테스트1427127', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테3', '테스트1278127', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테4', '테스트1271278', 3);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테5', '테스트1271727', 3);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테1', '테스트1276127', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테12', '테스트1271527', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테13', '테스트1274127', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테14', '테스트1273127', 3);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테15', '테스트1271272', 3);

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다111111111111111111.', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다22222222222222222222.', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다3333333333333.', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.444444444444444', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.555555555555', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.66666666666666', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.7777777777777777', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.88888888888888888', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.999999999999999999', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.10101010100101', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.11 11 11 11 11 11 11', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.12 12 12 12 12 12 12', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다. 13 13 13 13 13 13', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.14 14 14 14 14 14', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.15 15 15 15 15 15', now());

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.16 16 16 16 16', now());

---- 공지 임시 데이터
INSERT INTO notice_tb (title, content, view_count)
VALUES ('공지사항 1', '첫 번째 공지사항 내용입니다.', 0);
INSERT INTO notice_tb (title, content, view_count)
VALUES ('공지사항 2', '두 번째 공지사항 내용입니다.', 0);

INSERT INTO friend_tb (user_id, friend_id) VALUES
(1, 2),
(1, 3),
(2, 3),
(3, 4);

-- 친구 요청 테이블에 샘플 데이터 삽입
INSERT INTO friend_wait_tb (sender_id, receiver_id) VALUES
(2, 4),
(4, 1);

-- 아바타 테이블에 샘플 데이터 삽입
INSERT INTO avatar_tb (id, type, price, name) VALUES
(1, 1, 500, '아프로 헤어'),
(2, 2, 300, '양머리'),
(3, 3, 700, '배기팬츠'),
(4, 4, 400, '운동화'),
(5, 1, 600, '롱 헤어'),
(6, 3, 800, '정장 바지');

-- 유저 아이템 인벤토리 테이블에 샘플 데이터 삽입
INSERT INTO inventory_tb (user_id, avatar_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(2, 2),
(2, 3),
(2, 4),
(3, 5),
(3, 6),
(4, 6);

-- 현재 아바타 정보 테이블에 샘플 데이터 삽입
INSERT INTO now_avatar_tb (user_id, head, top, bottom, shoes) VALUES
(1, 1, NULL, 3, 4),
(2, 2, NULL, 3, NULL),
(3, 5, NULL, 6, NULL),
(4, NULL, NULL, 6, NULL);

-- 포인트 사용내역 테이블에 샘플 데이터 삽입
INSERT INTO point_history_tb (user_id, order_head, order_body, use_point, less_point) VALUES
(1, '아프로 헤어 외 3건', '아프로 헤어, 양머리, 배기팬츠, 운동화, 롱 헤어, 정장 바지 구입', 3300, 200),
(2, '양머리 외 2건', '양머리, 배기팬츠, 운동화 구입', 1400, 400),
(3, '롱 헤어 외 1건', '롱 헤어, 정장 바지 구입', 1400, 1000),
(4, '정장 바지 외 0건', '정장 바지 구입', 800, 500);

INSERT INTO emoticon_tb (url,name)
VALUES ('/image/emoticon/smile.jpg','smile');
