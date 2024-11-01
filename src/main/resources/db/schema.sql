-- 유저 테이블
create table user_tb (
  id int primary key auto_increment,
  user_id varchar(50) not null,
  name varchar(20)  null,
  password varchar(1000) not null,
  nickname varchar(20) null,
  UNIQUE (nickname),
  phone_number varchar(20)  null,
  gender varchar(10)  null,
  profile Blob,
  birth int  null,
  point int default 0,
  user_role int default 0,
  online_status boolean default false,
  active_status varchar(7) default '활동중',
  created_at timestamp default CURRENT_TIMESTAMP
);

-- 탈퇴 사유 조회 테이블
create table withdrawal_reason_tb (
  id int primary key auto_increment,
  user_id int not null,
  reason varchar(255) not null,
  details text null,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (user_id) references user_tb(id) on delete cascade
);


-- 방문 수 테이블
create table day_record_tb (
    user_id INT,
    date DATE,
    Unique(user_id,date),
    foreign key(user_id) references user_tb(id)
);

-- 학교 테이블
create table school_tb (
  id int primary key,
  name varchar(10) not null,
  region varchar(20) not null
);

-- 유저가 가진 학교 테이블
create table user_school_tb (
   user_id int,
   school_id int,
   primary key(user_id,school_id),
   foreign key (school_id) references school_tb(id),
   foreign key (user_id) references user_tb(id)
);

-- 게시글 테이블 (board_tb) - 참조되므로 먼저 생성
create table board_tb (
  id int primary key auto_increment,
  school_id int,
  title varchar(50),
  content_location varchar(255),
  image_location blob,
  user_id int,
  view_count int default 0,
  likes int default 0,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (school_id) references school_tb(id),
  foreign key (user_id) references user_tb(id)
);

-- 친구 테이블
create table friend_tb (
  user_id int,
  friend_id int,
  primary key(user_id, friend_id),
  foreign key (user_id) references user_tb(id) ON DELETE CASCADE,
  foreign key (friend_id) references user_tb(id) ON DELETE CASCADE
);

-- 친구 요청 테이블
create table friend_wait_tb (
  id int primary key auto_increment,
  sender_id int,
  receiver_id int,
  foreign key (sender_id) references user_tb(id) ON DELETE CASCADE,
  foreign key (receiver_id) references user_tb(id) ON DELETE CASCADE
);

-- 쪽지 테이블
create table message_tb (
  id int primary key auto_increment,
  receiver_user int,
  sender_user int,
  title varchar(15) not null,
  content varchar(255) not null,
  status int default 0,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (sender_user) references user_tb(id),
  foreign key (receiver_user) references user_tb(id)
);

-- 게시글 신고 테이블 (board_report_tb) - board_tb가 먼저 생성되므로 이후 생성 가능
create table board_report_tb (
  id int primary key auto_increment,
  sender_user int,
  board_id int,
  category varchar(10),
  content varchar(255),
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (sender_user) references user_tb(id),
  foreign key (board_id) references board_tb(id)
);

-- 유저 신고 테이블
create table user_report_tb (
  id int primary key auto_increment,
  sender_user int,
  receiver_user int,
  category varchar(15),
  content varchar(255),
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (sender_user) references user_tb(id),
  foreign key (receiver_user) references user_tb(id)
);

-- 광고 테이블
create table ad_tb (
  id int primary key auto_increment,
  title varchar(15),
  Image_location blob,
  view_count int,
  created_at timestamp default CURRENT_TIMESTAMP
);

-- 광고 조회수 테이블
create table ad_view_tb (
  user_id int,
  ad_id int,
  primary key(user_id, ad_id),
  foreign key (ad_id) references ad_tb(id)
);

-- 게시글 조회수 테이블
create table board_view_tb (
  user_id int,
  board_id int,
  primary key(user_id, board_id),
  foreign key (board_id) references board_tb(id) ON DELETE CASCADE
);

-- 좋아요 테이블
create table like_tb (
  id int primary key auto_increment,
  user_id int,
  board_id int,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (user_id) references user_tb(id),
  foreign key (board_id) references board_tb(id) ON DELETE CASCADE
);

-- 댓글 테이블
create table comment_tb (
  id int primary key auto_increment, -- pk
  board_id int, --게시글 id
  user_id int, --유저 id
  parent_id int, --대댓글의 부모 게시글 id , 일반 댓글은 null
  content_location varchar(255), -- 댓글, 대댓글 내용
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (board_id) references board_tb(id) ON DELETE CASCADE,
  foreign key (user_id) references user_tb(id)
);

-- 공지사항 테이블
create table notice_tb (
  id int primary key auto_increment,
  title varchar(15),
  content varchar(255),
  view_count int,
  created_at timestamp default CURRENT_TIMESTAMP
);

-- 공지사항 조회수 테이블
create table notice_view_tb (
  user_id int primary key auto_increment,
  notice_id int,
  foreign key (notice_id) references notice_tb(id)
);


-- 아바타 테이블
create table avatar_tb (
  id int primary key auto_increment,
  type int,
  price int,
  name varchar(255),
  url varchar(255)
);

-- 유저 아이템 인벤토리 테이블
create table inventory_tb (
  user_id int,
  avatar_id int,
  foreign key (avatar_id) references avatar_tb(id),
  foreign key (user_id) references user_tb(id),
  primary key(user_id, avatar_id)
);

-- 현재 아바타 정보 테이블
create table now_avatar_tb (
  user_id int primary key,
  head int not null default 9999,
  top int not null default 9998,
  bottom int not null default 9997,
  shoes int not null default 9996,
  foreign key (user_id) references user_tb(id),
  foreign key (head) references avatar_tb(id),
  foreign key (top) references avatar_tb(id),
  foreign key (bottom) references avatar_tb(id),
  foreign key (shoes) references avatar_tb(id)
);

-- 결제 내역 테이블
create table charge_history_tb (
    id int primary key auto_increment,
    user_id int,
    order_name varchar(100),
    order_id varchar(64),
    payment_key varchar(200) not null,
    point int,
    total_amount int,
    approved_at timestamp default CURRENT_TIMESTAMP,
    method varchar(30),
    refund_status varchar(30),
    foreign key (user_id) references user_tb(id)
);

-- 환불 내역 테이블
create table refund_tb (
    id int primary key auto_increment,
    payment_key varchar(200) not null,
    order_name varchar(100),
    order_id varchar(64),
    cancel_amount int,
    cancel_reason varchar(200) not null,
    requested_at timestamp default CURRENT_TIMESTAMP,
    canceled_at timeStamp default CURRENT_TIMESTAMP,
    cancel_status varchar(100)
);

-- 환불 신청 테이블
create table request_refund_tb (
    id int primary key auto_increment,
    charge_history_id int,
    cancel_reason varchar(200) not null,
    request_at timeStamp default CURRENT_TIMESTAMP,
    foreign key (charge_history_id) references charge_history_tb(id)
);

-- 환불 반려 사유 테이블
create table refund_refuse_tb(
    id int primary key auto_increment,
    charge_history_id int,
    refund_refuse_reason varchar(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    foreign key (charge_history_id) references charge_history_tb(id)
);

-- 이모티콘 테이블
create table emoticon_tb (
     id int primary key auto_increment,
     url varchar(255),
     name varchar(15)
);

-- 포인트 사용내역 테이블
create table point_history_tb(
    id int primary key auto_increment,
    user_id int, -- 유저id
    order_head varchar(30), -- 포인트 사용 간단 내용 ex) : 아프로 헤어 외 n건..
    order_body varchar(255), -- 포인트 사용 상세 내용 ex) : 아프로 헤어 , 양머리 두건, 배기팬츠 구입
    use_point int, -- 사용 포인트
    less_point int, -- 잔여 포인트
    created_at timestamp default CURRENT_TIMESTAMP,
    foreign key (user_id) references user_tb(id)
);

-- 채팅에 참가한 유저 리스트
create table chat_room_tb (
    id int primary key auto_increment, -- roomId(PK)
    user_id int,
    friend_id int,
    unique(user_id, friend_id),
    last_message_date DATE,
    foreign key (user_id) references user_tb(id),
    foreign key (friend_id) references user_tb(id)
);

-- 채팅 내역 저장 테이블
create table chat_log_tb (
    id int primary key auto_increment, -- pk
    room_id int not null,
    user_id int not null,
    type varchar (10),
    message varchar(255),
    created_at timestamp,
    foreign key (room_id) references chat_room_tb(id),
    foreign key (user_id) references user_tb(id)
);

-- 알람 tb
create table alarm_tb (
	id int primary key auto_increment,
	type varchar(20) not null,
	sender_id int not null,
  receiver_id int,
	content varchar(255),
  created_at timestamp default now(),
  status int default 0
);

create table prodHistory_tb(
    id int primary key auto_increment,
    avatar_id int,
    foreign key (avatar_id) references avatar_tb(id)
);
