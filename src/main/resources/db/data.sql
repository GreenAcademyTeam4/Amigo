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
values (1, '테스트', '테스트123123', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '테테테테텥테', '테스트123123', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '비가온다주륵주륵주륵주륵주륵', '주르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르르', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '오늘의 테스트', '얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍얍', 1);

-- 댓글 테이블에 임시 데이터 삽입
INSERT INTO comment_tb (board_id, user_id, content_location, created_at)
VALUES
(1, 1, '게시글 1에 대한 첫 번째 댓글입니다.', now());

---- 공지 임시 데이터
INSERT INTO notice_tb (title, content, view_count)
VALUES ('공지사항 1', '첫 번째 공지사항 내용입니다.', 0);
INSERT INTO notice_tb (title, content, view_count)
VALUES ('공지사항 2', '두 번째 공지사항 내용입니다.', 0);

INSERT INTO emoticon_tb (url,name)
VALUES ('/image/emoticon/smile.jpg','smile');
