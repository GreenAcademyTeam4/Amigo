insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('thddnjstjr','1234','고죠','고죠','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test',1234,'가을','정훈','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth)
values('test1',1234,'겨울','짱구','010-1234-5678','male','27');

insert into user_tb (user_id,password,name,nickname,phone_number,gender,birth,elementary_school, middle_school, high_school)
values('diqdiq',1234,'봄','테스트얍','010-1234-5678','male','27', '가초등학교', '나중학교', '다대학교');

-- 임시 데이터 학교
INSERT INTO school_tb (id, school, name, region) VALUES
(1, 'ABC School', 'ABC', 'Seoul'),
(2, 'XYZ School', 'XYZ', 'Busan');

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

