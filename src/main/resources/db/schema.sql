-- 유저 테이블
create table user_tb (
  id int primary key auto_increment,
  user_id varchar(20) not null,
  name varchar(20) not null,
  password varchar(20) not null,
  nickname varchar(20) null,
  UNIQUE (nickname),
  phone_number varchar(20) not null,
  user_role varchar(20) null,
  elementary_school varchar(20) null,
  midle_school varchar(20) null,
  high_school varchar(20) null, 
  gender varchar(10) not null,
  birth int not null,
  point int default 0,
  online_status int default 0,
  active_status int default 0,
  created_at timestamp default CURRENT_TIMESTAMP
);

-- 학교 테이블
create table school_tb (
  id int primary key auto_increment,
  school varchar(20),
  name varchar(10) not null,
  region varchar(10) not null
);

-- 게시글 테이블 (board_tb) - 참조되므로 먼저 생성
create table board_tb (
  id int primary key auto_increment,
  school_id int,
  title varchar(15),
  content_location varchar(20),
--  Image_location blob,
  user_id int,
  view_count int,
  likes int,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (school_id) references school_tb(id),
  foreign key (user_id) references user_tb(id)
);

-- 친구 테이블
create table friend_tb (
  user_id int primary key,
  friend_id int,
  foreign key (friend_id) references user_tb(id)
);

-- 친구 요청 테이블
create table friend_wait_tb (
  id int primary key auto_increment,
  sender_id int,
  receiver_id int,
  foreign key (sender_id) references user_tb(id),
  foreign key (receiver_id) references user_tb(id)
);

-- 알람 테마 테이블
create table alarm_theme_tb (
  id int primary key auto_increment,
  name varchar(20)
);

-- 알람 테이블
create table alarm_tb (
  id int primary key auto_increment,
  sender_user int,
  receiver_user int,
  theme int,
  content varchar(255),
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (theme) references alarm_theme_tb (id),
  foreign key (sender_user) references user_tb(id),
  foreign key (receiver_user) references user_tb(id)
);

-- 쪽지 테이블
create table message_tb (
  id int primary key auto_increment,
  receiver_user int,
  sender_user int,
  title varchar(15) not null,
  content varchar(15) not null,
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
  user_id int primary key auto_increment,
  ad_id int,
  foreign key (ad_id) references ad_tb(id)
);

-- 게시글 조회수 테이블
create table board_view_tb (
  user_id int primary key auto_increment,
  board_id int,
  foreign key (board_id) references board_tb(id)
);

-- 좋아요 테이블
create table like_tb (
  id int primary key auto_increment,
  user_id int,
  board_id int,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (user_id) references user_tb(id),
  foreign key (board_id) references board_tb(id)
);

-- 댓글 테이블
create table comment_tb (
  id int primary key auto_increment,
  board_id int,
  user_id int,
  content_location blob,
  created_at timestamp default CURRENT_TIMESTAMP,
  foreign key (board_id) references board_tb(id),
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

-- 아바타 타입 테이블
create table avatar_type_tb (
  id int primary key auto_increment,
  name varchar(10)
);

-- 아바타 테이블
create table avatar_tb (
  id int primary key auto_increment,
  name varchar(255),
  type int,
  foreign key (type) references avatar_type_tb(id)
);

-- 유저 아이템 인벤토리 테이블
create table inventory_tb (
  user_id int primary key,
  avatar_id int,
  foreign key (avatar_id) references avatar_tb(id)
);

-- 현재 아바타 정보 테이블
create table now_avatar_tb (
  user_id int primary key,
  head int,
  top int,
  bottom int,
  shoes int,
  foreign key (head) references avatar_tb(id),
  foreign key (top) references avatar_tb(id),
  foreign key (bottom) references avatar_tb(id),
  foreign key (shoes) references avatar_tb(id)
);

create table charge_history_tb (
    id int primary key auto_increment,
    user_id int,
    order_name varchar(100),
    order_id varchar(100),
    point int,
    total_amount int,
    approved_at timestamp default CURRENT_TIMESTAMP,
    method varchar(20),
    payment_key varchar(100),
    foreign key (user_id) references user_tb(id)
);