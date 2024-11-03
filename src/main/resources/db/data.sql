insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth, point ,online_status)
values('thddnjstjr','1234','원석','고죠 사토루','010-1234-5678','male','19980115', 50000 ,true);

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth,online_status)
values('test','1234','정훈','게토 스구루','010-1234-5678','male','19980225',true);

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test','1234','가을','정훈','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test1','5996ec04-eebf-42e2-a05e-a9aaf02095cf','겨울','짱구','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test2','1234','봄','도라에몽','010-1234-5678','female','17');

-- 임시 데이터
insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values ('test2', '1234', '가을2', '구구2', '010-1234-5678', 'male', '27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values ('test3', '1234', '겨울1', '짱구3', '010-1234-5678', 'male', '27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values ('test4', '1234', '봄3', '테스트얍12', '010-1234-5678', 'male', '27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values ('test5', '1234', '여름4', '테스터4', '010-1234-5678', 'male', '27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values ('test6', '1234', '가을5', '테스트6', '010-1234-5678', 'male', '27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values ('test7', '1234', '겨울6', '테스트7', '010-1234-5678', 'male', '27');

-- 임시 데이터 학교
INSERT INTO school_tb (id, name, region) VALUES
(1,'ABC', 'Seoul'),
(2,'XYZ', 'Busan');

-- 임시 데이터 유저 신고 (나중에 삭제하기)
INSERT INTO user_report_tb (sender_user, receiver_user, category, content)
VALUES
(1, 2, '부적절한 언행', '도라에몽이 게임에서 비매너 발언을 했습니다.');

-- 결제 임시 데이터
insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1,000원', 'MC4yMjkyMzc5MTMyMDA3', 1000, 1000, '2024-10-21 12:50:31', '휴대폰', 'tviva20241021124957fLyI0', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (2, '포인트 충전1,000원', 'MC4yNTM2NTY3NjQzODc3', 1000, 1000, '2024-10-25 18:21:23', '휴대폰', 'tviva20241025182058fESt7', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (2, '포인트 충전1,000원', 'MC4yNjcxMTU1ODY1NTIw', 1000, 1000, '2024-10-26 18:21:23', '휴대폰', 'tviva20241025182937rTWv7', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (2, '포인트 충전1,000원', 'MC4xNzQ5NzM4MTQwMDE0', 1000, 1000, '2024-10-28 18:21:23', '휴대폰', 'tviva20241028093632rKXj1', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (2, '포인트 충전1,000원', 'MC4wMzY5MTA2MDk5MTIy', 1000, 1000, '2024-10-29 14:39:01', '휴대폰', 'tviva20241028143745iHv15', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1,000원', 'MC40MTk1MzczMzQzMDI5', 1000, 1000, '2024-10-28 14:39:01', '간편결제', 'tviva20241028164844i11C4', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전3,000원', 'MC43NjQyMDExNTgzMjM4', 3000, 3000, '2024-10-29 15:14:41', '카드', 'tviva202410291510575csH7', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1,000원', 'MC4yNzk5MjI2MTE0MTA1', 1000, 1000, '2024-10-29 16:23:27', '휴대폰', 'tviva202410291622277ipp7', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전3,000원', 'MC44NTA2MzM1MDc4MDQ1', 3000, 3000, '2024-10-30 16:23:30', '휴대폰', 'tviva20241030162248JlTT9', 'none');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전500,000원', 'MC43NzE0MDYzNDcyMTQw', 500000, 500000, '2024-11-01 11:15:09', '간편결제', 'tviva20241101111408StJK2', 'none');

insert into refund_tb(payment_key, order_name, order_id, cancel_amount, cancel_reason, requested_at, canceled_at, cancel_status)
        VALUES ('tviva20241030162248JlTT9', '포인트 충전3,000원', 'MC44NTA2MzM1MDc4MDQ1', 3000, '단순 변심', '2024-10-30 16:22:48', '2024-10-30 16:25:37', 'DONE');

insert into charge_history_tb(user_id, order_name, order_id, point, total_amount, approved_at, method, payment_key, refund_status)
        VALUES (1, '포인트 충전1000원', 'MC40MTk1MzczMzQzMDI5', 1000, 1000, '2024-11-02 14:39:01', '간편결제', 'tviva20241028164844i11C4', 'none');

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

--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다111111111111111111.', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다22222222222222222222.', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다3333333333333.', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.444444444444444', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.555555555555', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.66666666666666', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.7777777777777777', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.88888888888888888', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.999999999999999999', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.10101010100101', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.11 11 11 11 11 11 11', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.12 12 12 12 12 12 12', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다. 13 13 13 13 13 13', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.14 14 14 14 14 14', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.15 15 15 15 15 15', now());
--
---- 댓글 테이블에 임시 데이터 삽입
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.16 16 16 16 16', now());
--
--
--INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
--VALUES
--(1, 1, '첫 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(1, 2, '첫 번째 게시글에 대한 두 번째 댓글입니다.', NOW()),
--(1, 3, '첫 번째 게시글에 대한 세 번째 댓글입니다.', NOW()),
--(2, 1, '두 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(2, 2, '두 번째 게시글에 대한 두 번째 댓글입니다.', NOW()),
--(3, 1, '세 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(4, 2, '네 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(5, 3, '다섯 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(5, 1, '다섯 번째 게시글에 대한 두 번째 댓글입니다.', NOW()),
--(6, 2, '여섯 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(7, 3, '일곱 번째 게시글에 대한 첫 번째 댓글입니다.', NOW()),
--(7, 1, '일곱 번째 게시글에 대한 두 번째 댓글입니다.', NOW());


---- 공지 임시 데이터
INSERT INTO notice_tb (title, content, view_count)
VALUES ('공지사항 1', '첫 번째 공지사항 내용입니다.', 0);
INSERT INTO notice_tb (title, content, view_count)
VALUES ('공지사항 2', '두 번째 공지사항 내용입니다.', 0);



-- 게시글 데이터 삽입
INSERT INTO board_tb (school_id, title, content_location, user_id, view_count, likes, created_at)
VALUES (1, '첫11 번째 게시글', '첫 번째 게시글 내용 위치', 1, 100, 2, NOW()); -- 23

INSERT INTO board_tb (school_id, title, content_location, user_id, view_count, likes, created_at)
VALUES (1, '두22 번째 게시글', '두 번째 게시글 내용 위치', 2, 150, 3, NOW()); -- 24

INSERT INTO board_tb (school_id, title, content_location, user_id, view_count, likes, created_at)
VALUES (1, '세33 번째 게시글', '세 번째 게시글 내용 위치', 3, 200, 4, NOW()); -- 25

INSERT INTO board_tb (school_id, title, content_location, user_id, view_count, likes, created_at)
VALUES (1, '네44 번째 게시글', '세 번째 게시글 내용 위치', 3, 300, 6, NOW()); -- 26

INSERT INTO board_tb (school_id, title, content_location, user_id, view_count, likes, created_at)
VALUES (1, '다섯55 번째 게시글', '세 번째 게시글 내용 위치', 4, 250, 7, NOW()); -- 27

-- like_tb 데이터 삽입
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (1, 23, NOW()); -- 첫 번째 게시글을 좋아요한 유저 1
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (2, 23, NOW()); -- 첫 번째 게시글을 좋아요한 유저 2
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (3, 24, NOW()); -- 두 번째 게시글을 좋아요한 유저 3
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (4, 24, NOW()); -- 두 번째 게시글을 좋아요한 유저 4
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (1, 24, NOW()); -- 두 번째 게시글을 좋아요한 유저 1
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (1, 25, NOW()); -- 세 번째 게시글을 좋아요한 유저 1
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (2, 25, NOW()); -- 세 번째 게시글을 좋아요한 유저 2
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (3, 25, NOW()); -- 세 번째 게시글을 좋아요한 유저 3
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (4, 25, NOW()); -- 세 번째 게시글을 좋아요한 유저 4
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (1, 26, NOW()); -- 네 번째 게시글을 좋아요한 유저 1
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (2, 26, NOW()); -- 네 번째 게시글을 좋아요한 유저 2
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (3, 26, NOW()); -- 네 번째 게시글을 좋아요한 유저 3
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (4, 26, NOW()); -- 네 번째 게시글을 좋아요한 유저 4
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (5, 26, NOW()); -- 네 번째 게시글을 좋아요한 유저 5
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (6, 26, NOW()); -- 네 번째 게시글을 좋아요한 유저 6
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (1, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 1
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (2, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 2
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (3, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 3
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (4, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 4
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (5, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 5
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (6, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 6
INSERT INTO like_tb (user_id, board_id, created_at) VALUES (7, 27, NOW()); -- 다섯 번째 게시글을 좋아요한 유저 7



-- 댓글 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES (1, 1, '첫 번째 게시글에 대한 첫 번째 댓글', NOW());

INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES (1, 2, '첫 번째 게시글에 대한 두 번째 댓글', NOW());

INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES (2, 3, '두 번째 게시글에 대한 첫 번째 댓글', NOW());

INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES (2, 1, '두 번째 게시글에 대한 두 번째 댓글', NOW());

INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES (3, 4, '세 번째 게시글에 대한 첫 번째 댓글', NOW());

INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES (3, 2, '세 번째 게시글에 대한 두 번째 댓글', NOW());


INSERT INTO like_tb (user_id, board_id) VALUES (7, 7);
INSERT INTO like_tb (user_id, board_id) VALUES (2, 7);
INSERT INTO like_tb (user_id, board_id) VALUES (3, 7);
INSERT INTO like_tb (user_id, board_id) VALUES (5, 7);
INSERT INTO friend_tb (user_id, friend_id) VALUES
(1, 2),
(2, 1),
(1, 3),
(3, 1);

-- 아바타 테이블에 샘플 데이터 삽입
INSERT INTO avatar_tb (id, type, price, name) VALUES
(1, 1, 500, '아프로 헤어'),
(2, 1, 300, '양머리'),
(3, 3, 700, '배기팬츠'),
(4, 4, 400, '운동화'),
(5, 1, 600, '롱 헤어'),
(6, 3, 800, '정장 바지'),
(7, 1, 400, '폭풍간지컷'),
(8, 2, 700, '탱크톱'),
(9, 3, 1200, '돌핀팬츠'),
(10, 4, 600, '캔버스화');

-- 유저 아이템 인벤토리 테이블에 샘플 데이터 삽입
INSERT INTO inventory_tb (user_id, avatar_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(2, 1),
(2, 2),
(2, 3),
(2, 4),
(2, 5),
(2, 6),
(2, 7),
(2, 8),
(2, 9),
(2, 10),
(3, 1),
(3, 2),
(3, 3),
(3, 4),
(3, 5),
(3, 6),
(3, 7),
(3, 8),
(3, 9),
(3, 10),
(4, 1),
(4, 2),
(4, 3),
(4, 4),
(4, 5),
(4, 6),
(4, 7),
(4, 8),
(4, 9),
(4, 10);

-- 현재 아바타 정보 테이블에 샘플 데이터 삽입
INSERT INTO now_avatar_tb (user_id, head, top, bottom, shoes) VALUES
(1, 1, 8, 3, 4),
(2, 2, 8, 3, 4),
(3, 5, 8, 6, 4),
(4, 2, 8, 6, 4);

-- 포인트 사용내역 테이블에 샘플 데이터 삽입
INSERT INTO point_history_tb (user_id, order_head, order_body, use_point, less_point) VALUES
(1, '아프로 헤어 외 3건', '아프로 헤어, 양머리, 배기팬츠, 운동화, 롱 헤어, 정장 바지 구입', 3300, 200),
(2, '양머리 외 2건', '양머리, 배기팬츠, 운동화 구입', 1400, 400),
(3, '롱 헤어 외 1건', '롱 헤어, 정장 바지 구입', 1400, 1000),
(4, '정장 바지 외 0건', '정장 바지 구입', 800, 500);

-- 샘플 이모티콘
INSERT INTO emoticon_tb (url,name)
VALUES ('/image/emoticon/smile.jpg','smile');

insert into user_school_tb (user_id,school_id)
values (1,1),
       (2,1),
        (1,2),
        (2,2);