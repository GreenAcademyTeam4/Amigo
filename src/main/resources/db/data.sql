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
(1,'용문중학교', 'Seoul'),
(2,'용호초등학교', 'Busan');

-- 임시 데이터 유저 신고 (나중에 삭제하기)
INSERT INTO user_report_tb (sender_user, receiver_user, category, content)
VALUES
(1, 2, '부적절한 언행', '도라에몽이 게임에서 비매너 발언을 했습니다.');

---- 게시글 신고 임시 데이터
INSERT INTO board_report_tb (sender_user, category, content)
VALUES
  (1, '욕설', '게시글에 부적절한 언어가 사용되었습니다.');

-- 임시 데이터 탈퇴 사유 (나중에 삭제)
INSERT INTO withdrawal_reason_tb (user_id, reason, details)
VALUES (1, '서비스 불만족', '원하는 기능이 부족합니다.');

-- user_tb 테이블에 탈퇴 중인 유저 데이터 삽입
INSERT INTO user_tb (user_id, name, password, nickname, phone_number, gender, birth, active_status, created_at)
VALUES
    ('kkk', '홍길동', '1234', '얍얍', '010-1234-5678', 'male', 25, '탈퇴', CURRENT_TIMESTAMP),
    ('mmm', '뿡뿡', '1234', '뿡뿡', '010-8765-4321', 'female', 30, '탈퇴', CURRENT_TIMESTAMP);
-- 탈퇴 사유 임시 데이터
INSERT INTO withdrawal_reason_tb (user_id, reason, details, created_at)
VALUES
    (12, '서비스 불만족', '기능이 부족하고 개선이 필요합니다.', CURRENT_TIMESTAMP),
    (13, '개인정보 보호 우려', '데이터 보안에 대한 불안감이 있습니다.', CURRENT_TIMESTAMP);


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
values (1, '궁금한게 있는데 물어봐도 되나요?', '채팅은 어디서 하나요?', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '이거 점검이 언제인가요?', '점검 이후에 상점 이용할 수 있나요?', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '채팅 기능 사용해 보신분?', '채팅 작동이 잘 되나요?', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '기능 업데이트는 언제 하나요?', '곧 추가된다고 한거 같아서요', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '게시글에 어떻게 문의하나요?', '문의 방법을 알려주세요', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '혹시 ABC SCHOOL 맞나요', '반갑습니다.', 1);

---- 임시 데이터 게시글 -- 방금 likes 가 7이라서 추가해 봄
insert into board_tb (school_id, title, content_location, user_id, likes)
values (1, '동창회 일정이 있나요?', '일정이 있나요?', 1, 4);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '정기 모임도 하나요?', '모임이 있는지 궁금해서 질문합니다.', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '혹시 55회 졸업생분들 계신가요?', '55회 졸업생 분들?', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '가을 축제 리스트 올려요', '가을 축제 리스트는 1. XXX ', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '등산 동호회 만들면 가입하실분 계신가요?', '동호회 만들기', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '혹시 xxx 선생님 은퇴하셨나요?', '궁금해서 물어봅니다.', 1);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, 'ooo 선생님 담임이었던 분들 계신가요?', '옛날생각나서 게시글을 올립니다.', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '결제는 어디서 할수 있나요?', '결제를 어디서 하는지 몰라서 물어봅니다.', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '학교 주변 맛집 추천 부탁해요!!', '다음 모임 때 가볼까 해요!', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '이번 추억 사진 앨범 공유합니다.', '다들 함께 찍었던 사진들 있어요', 3);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '혹시 학교 운동장 개방 시간 아시나요?', '가끔 운동하러 가고 싶어서요.', 3);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '이제 다들 어떤 일 하시나요?', '각자 하는 일 공유해 봐요.', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '우리 반 추억의 선생님들 기억나세요?', '정말 즐거웠던 추억이었죠', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '55회 졸업생들 동창회 모임 만들까요?', '참여하실 분들 의견 부탁드려요.', 2);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '다음 모임 장소로 좋은 곳 추천 부탁드립니다.', '다음 모임에서 만날 장소로 어디가 좋을까요?', 3);

---- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (1, '추억 여행 겸 여행지 추천 부탁해요.', '옛 친구들과 가기 좋은 여행지 있으면 추천해 주세요.', 3);


-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '동창회 일정 확정됐나요?', '모임 날짜 어떻게 됐는지 궁금합니다.', 1);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '졸업한 지 벌써 몇 년이네요!', '다들 어떻게 지내시나요?', 1);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '55회 졸업생 모임 있나요?', '참여하실 분들 있으신가요?', 1);


-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id, image_location, view_count, likes, created_at)
values (2, '학교 근처 맛집 추천 부탁드립니다.', '다음 모임 때 가볼까 해요!', 1, NULL, 6, 6, NOW());

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '옛날 선생님들 소식 아시는 분?', '특히 ooo 선생님이 그립네요.', 1);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '이번 동창회 사진 모아봤어요.', '사진첩 공유합니다. 추억이네요!', 2);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '학교 운동장 개방 시간 아시나요?', '운동하러 가보고 싶어서요.', 2);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '졸업 후 첫 모임 계획해 볼까요?', '모두 모여서 즐거운 시간 가져요!', 2);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '이번에 동창회 회비는 얼마인가요?', '회비 정보 부탁드립니다.', 2);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '자녀분들 근황도 궁금하네요.', '다들 자녀들 이야기 좀 해요!', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '다음 모임 장소로 괜찮은 곳 있을까요?', '추천할 만한 장소 있으면 알려주세요.', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '추억 여행지 추천 부탁드려요.', '옛 친구들과 갈 만한 곳 알려주세요.', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '우리 반 추억의 이야기들 기억나세요?', '정말 즐거운 시간들이었죠.', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '동창 모임에서 있었던 에피소드', '정말 재밌었던 순간들 공유해요!', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '모임 후 뒷풀이 장소로 좋은 곳 추천해주세요.', '마무리 장소로 괜찮은 곳 아시는 분?', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '55회 졸업생 모임 만들까요?', '참여하고 싶으신 분들 알려주세요!', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '다음 모임에 참여하실 분들 모집합니다.', '같이 즐거운 시간 보내요!', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '졸업한 지 꽤 됐네요. 다들 보고 싶어요.', '오랜만에 모일 생각하니 설레네요.', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '혹시 우리 학교 근처 카페 추천해 주세요.', '모임 후 가볼 만한 곳 찾고 있어요.', 3);

-- 임시 데이터 게시글
insert into board_tb (school_id, title, content_location, user_id)
values (2, '새로 생긴 동호회에 가입하실 분 있나요?', '같이 활동할 사람들 모아요!', 3);


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
INSERT INTO avatar_tb (id, type, price, name,url) VALUES
(1, 1, 500, '아프로 헤어','/image/avator/avatar1.jpg'),
(2, 1, 300, '우주해적','/image/avator/avatar2.jpg'),
(3, 1, 700, '하츠네미쿠','/image/avator/avatar3.png'),
(4, 4, 400, '운동화',null),
(5, 1, 600, '롱 헤어',null),
(6, 3, 800, '정장 바지',null),
(7, 1, 400, '폭풍간지컷',null),
(8, 2, 700, '탱크톱',null),
(9, 3, 1200, '돌핀팬츠',null),
(10, 4, 600, '캔버스화',null);

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

-- 기본 아바타 삽입
INSERT INTO avatar_tb (id, type, price, name, url) VALUES
      (11,1,0,'남자 기본 아바타','/image/avator/male_body.png'),
      (12,1,0,'여자 기본 아바타','/image/avator/female_body.png');

-- 유저가 장착한 아바타
INSERT INTO now_avatar_tb (user_id,head, top, bottom, shoes) VALUES
      (1,11,1,2,3),
      (2,11,1,2,3);



