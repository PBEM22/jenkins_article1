DROP TABLE IF EXISTS blame;
DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS reply;
DROP TABLE IF EXISTS picture;
DROP TABLE IF EXISTS select_outfit;
DROP TABLE IF EXISTS outfit_style;
DROP TABLE IF EXISTS outfit_situation;
DROP TABLE IF EXISTS board;
DROP TABLE IF EXISTS style;
DROP TABLE IF EXISTS preference;
DROP TABLE IF EXISTS ban;
DROP TABLE IF EXISTS select_record;
DROP TABLE IF EXISTS situation;
DROP TABLE IF EXISTS outfit;
DROP TABLE IF EXISTS user;

ALTER DATABASE article CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE user (
                      user_seq BIGINT NOT NULL AUTO_INCREMENT,
                      user_social_site VARCHAR(50) NOT NULL COMMENT 'KAKAO, NAVER, GOOGLE',
                      user_id VARCHAR(255) NOT NULL,
                      user_name VARCHAR(50) NOT NULL,
                      user_nickname VARCHAR(50) NULL,
                      user_phone_num VARCHAR(255) NULL,
                      user_birth_date DATE NULL,
                      user_gender VARCHAR(50) NULL COMMENT 'MALE, FEMALE',
                      user_state VARCHAR(50) NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, BAN, DELETE',
                      reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                      del_date DATETIME NULL,
                      user_auth VARCHAR(50) NOT NULL DEFAULT 'USER' COMMENT 'USER, ADMIN',
                      PRIMARY KEY (user_seq)
);

CREATE TABLE preference (
                            user_seq BIGINT NOT NULL AUTO_INCREMENT,
                            preference_condition VARCHAR(50) NOT NULL DEFAULT 'NORMAL' COMMENT 'HOT, COLD, NORMAL',
                            preference_style VARCHAR(50) NOT NULL DEFAULT 'NORMAL' COMMENT 'CASUAL, SPORTY, FORMAL, NORMAL',
                            PRIMARY KEY (user_seq)
);

CREATE TABLE outfit (
                        outfit_seq BIGINT NOT NULL AUTO_INCREMENT,
                        outfit_name VARCHAR(50) NOT NULL,
                        outfit_weather INT NULL COMMENT '악세서리만',
                        outfit_temp_max DOUBLE NULL,
                        outfit_temp_min DOUBLE NULL,
                        outfit_category VARCHAR(50) NOT NULL COMMENT '상의, 하의, 아우터, 신발, 악세사리',
                        outfit_gender VARCHAR(50) NOT NULL COMMENT 'M, F, N (남, 여, 무관)',
                        outfit_level VARCHAR(50) NULL COMMENT '권장, 필수, 선택',
                        outfit_img VARCHAR(500) NOT NULL,
                        PRIMARY KEY (outfit_seq)
);

CREATE TABLE style (
                       style_seq BIGINT NOT NULL AUTO_INCREMENT,
                       style_name VARCHAR(50) NOT NULL COMMENT '캐주얼, 포멀, 스포티',
                       PRIMARY KEY (style_seq)
);

CREATE TABLE situation (
                           situation_seq BIGINT NOT NULL AUTO_INCREMENT,
                           situation_name VARCHAR(50) NOT NULL COMMENT '일상, 여행, 운동, 데이트, 격식있는자리',
                           PRIMARY KEY (situation_seq)
);

CREATE TABLE select_record (
                               select_seq BIGINT NOT NULL AUTO_INCREMENT,
                               user_seq BIGINT NOT NULL,
                               situation_seq BIGINT NOT NULL,
                               select_date DATETIME NOT NULL,
                               custom_date DATETIME NOT NULL,
                               custom_location VARCHAR(50) NOT NULL,
                               weather_code VARCHAR(255) NOT NULL,
                               high_temp DOUBLE NOT NULL,
                               low_temp DOUBLE NOT NULL,
                               daily_temp DOUBLE NOT NULL,
                               cur_temp DOUBLE NOT NULL,
                               precipitation VARCHAR(255) NULL,
                               PRIMARY KEY (select_seq)
);

CREATE TABLE board (
                       board_seq BIGINT NOT NULL AUTO_INCREMENT,
                       user_seq BIGINT NOT NULL,
                       board_title VARCHAR(50) NOT NULL,
                       board_content VARCHAR(1000) NOT NULL,
                       reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                       up_date DATETIME NULL,
                       del_date DATETIME NULL,
                       board_is_blind BOOLEAN NOT NULL DEFAULT FALSE,
                       board_is_notice BOOLEAN NOT NULL DEFAULT FALSE COMMENT '관리자만 공지사항 작성 가능',
                       PRIMARY KEY (board_seq)
);

CREATE TABLE review (
                        review_seq BIGINT NOT NULL,
                        user_seq BIGINT NOT NULL,
                        select_seq BIGINT NOT NULL,
                        review_content VARCHAR(255) NOT NULL,
                        review_weather DECIMAL(10, 2) NULL,
                        review_location DECIMAL(10, 2) NULL,
                        review_blind BOOLEAN NOT NULL,
                        review_like_yn BOOLEAN NULL,
                        review_report INT NULL,
                        reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                        up_date DATETIME NULL DEFAULT LOCALTIME,
                        del_date DATETIME NULL DEFAULT LOCALTIME,
                        PRIMARY KEY (review_seq, user_seq)
);

CREATE TABLE blame (
                       blame_seq BIGINT NOT NULL AUTO_INCREMENT,
                       blame_user_seq BIGINT NOT NULL,
                       blame_board_seq BIGINT NULL,
                       blame_reply_seq BIGINT NULL,
                       blame_review_seq BIGINT NULL,
                       blame_processingdate DATETIME NULL,
                       reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                       up_date DATETIME NULL,
                       blame_status BOOLEAN NOT NULL DEFAULT TRUE COMMENT '처리 상태 (TRUE: 미처리, FALSE: 처리됨)',
                       PRIMARY KEY (blame_seq)
);

CREATE TABLE reply (
                       reply_seq BIGINT NOT NULL AUTO_INCREMENT,
                       board_seq BIGINT NOT NULL,
                       reply_user_seq BIGINT NOT NULL,
                       reply_content VARCHAR(1000) NOT NULL,
                       reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                       del_date DATETIME NULL,
                       reply_is_blind BOOLEAN NOT NULL DEFAULT FALSE,
                       PRIMARY KEY (reply_seq)
);

CREATE TABLE picture (
                         picture_seq BIGINT NOT NULL AUTO_INCREMENT,
                         picture_board_seq BIGINT NOT NULL,
                         picture_originename VARCHAR(255) NOT NULL,
                         picture_changedname VARCHAR(255) NOT NULL,
                         picture_url VARCHAR(255) NOT NULL,
                         picture_type VARCHAR(100) NOT NULL,
                         reg_date DATETIME NOT NULL DEFAULT LOCALTIME,
                         del_date DATETIME NULL,
                         picture_is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
                         PRIMARY KEY (picture_seq)
);

CREATE TABLE select_outfit (
                                 select_outfit_seq BIGINT NOT NULL AUTO_INCREMENT,
                                 outfit_seq BIGINT NOT NULL,
                                 select_seq BIGINT NOT NULL,
                                 PRIMARY KEY (select_outfit_seq)
);

CREATE TABLE outfit_style (
                              select_style_seq BIGINT NOT NULL AUTO_INCREMENT,
                              style_seq BIGINT NOT NULL,
                              outfit_seq BIGINT NOT NULL,
                              PRIMARY KEY (select_style_seq)
);

CREATE TABLE outfit_situation (
                                  outfit_situation_seq BIGINT NOT NULL AUTO_INCREMENT,
                                  outfit_seq BIGINT NOT NULL,
                                  situation_seq BIGINT NOT NULL,
                                  PRIMARY KEY (outfit_situation_seq)
);

CREATE TABLE ban (
                     ban_seq BIGINT NOT NULL AUTO_INCREMENT,
                     ban_user_seq BIGINT NOT NULL,
                     ban_startdate DATETIME NOT NULL DEFAULT LOCALTIME,
                     ban_enddate DATETIME NOT NULL,
                     ban_releasedate DATETIME NULL COMMENT '특별 사면되면 기입하는 컬럼',
                     PRIMARY KEY (ban_seq)
);


ALTER TABLE preference ADD CONSTRAINT FK_user_TO_preference FOREIGN KEY (user_seq) REFERENCES user (user_seq);
ALTER TABLE select_record ADD CONSTRAINT FK_user_TO_select_record FOREIGN KEY (user_seq) REFERENCES user (user_seq);
ALTER TABLE select_record ADD CONSTRAINT FK_situation_TO_select_record FOREIGN KEY (situation_seq) REFERENCES situation (situation_seq);
ALTER TABLE board ADD CONSTRAINT FK_user_TO_board FOREIGN KEY (user_seq) REFERENCES user (user_seq);
ALTER TABLE review ADD CONSTRAINT FK_user_TO_review FOREIGN KEY (user_seq) REFERENCES user (user_seq);
ALTER TABLE review ADD CONSTRAINT FK_select_TO_review FOREIGN KEY (select_seq) REFERENCES select_record (select_seq);
ALTER TABLE blame ADD CONSTRAINT FK_user_TO_blame FOREIGN KEY (blame_user_seq) REFERENCES user (user_seq);
ALTER TABLE blame ADD CONSTRAINT FK_board_TO_blame FOREIGN KEY (blame_board_seq) REFERENCES board (board_seq);
ALTER TABLE blame ADD CONSTRAINT FK_review_TO_blame FOREIGN KEY (blame_review_seq) REFERENCES review (review_seq);
ALTER TABLE reply ADD CONSTRAINT FK_board_TO_reply FOREIGN KEY (board_seq) REFERENCES board (board_seq);
ALTER TABLE reply ADD CONSTRAINT FK_user_TO_reply FOREIGN KEY (reply_user_seq) REFERENCES user (user_seq);
ALTER TABLE picture ADD CONSTRAINT FK_board_TO_picture FOREIGN KEY (picture_board_seq) REFERENCES board (board_seq);
ALTER TABLE select_outfit ADD CONSTRAINT FK_outfit_TO_select_outfit FOREIGN KEY (outfit_seq) REFERENCES outfit (outfit_seq);
ALTER TABLE select_outfit ADD CONSTRAINT FK_select_TO_select_outfit FOREIGN KEY (select_seq) REFERENCES select_record (select_seq);
ALTER TABLE outfit_style ADD CONSTRAINT FK_style_TO_outfit_style FOREIGN KEY (style_seq) REFERENCES style (style_seq);
ALTER TABLE outfit_style ADD CONSTRAINT FK_outfit_TO_outfit_style FOREIGN KEY (outfit_seq) REFERENCES outfit (outfit_seq);
ALTER TABLE outfit_situation ADD CONSTRAINT FK_outfit_TO_outfit_situation FOREIGN KEY (outfit_seq) REFERENCES outfit (outfit_seq);
ALTER TABLE outfit_situation ADD CONSTRAINT FK_situation_TO_outfit_situation FOREIGN KEY (situation_seq) REFERENCES situation (situation_seq);
ALTER TABLE ban ADD CONSTRAINT FK_user_TO_ban FOREIGN KEY (ban_user_seq) REFERENCES user (user_seq);