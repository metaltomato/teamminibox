CREATE TABLE IF NOT EXISTS tb_post (
    id              bigint(20)    NOT NULL AUTO_INCREMENT COMMENT 'PK',
    title           varchar(100)  NOT NULL COMMENT '제목',
    content         varchar(3000) NOT NULL COMMENT '내용',
    writer          varchar(20)   NOT NULL COMMENT '작성자',
    view_cnt        int(11)       NOT NULL COMMENT '조회 수',
    notice_yn       tinyint(1)    NOT NULL COMMENT '공지글 여부',
    delete_yn       tinyint(1)    NOT NULL COMMENT '삭제 여부',
    created_date    datetime      NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
    modified_date   datetime               DEFAULT NULL COMMENT '최종 수정일시',
    PRIMARY KEY (id)
    ) COMMENT '게시글';

CREATE TABLE IF NOT EXISTS tb_comment (
                                          id              bigint       NOT NULL AUTO_INCREMENT COMMENT '댓글 번호 (PK)',
                                          post_id         bigint       NOT NULL COMMENT '게시글 번호 (FK)',
                                          content         varchar(1000) NOT NULL COMMENT '내용',
    writer          varchar(20)   NOT NULL COMMENT '작성자',
    delete_yn       tinyint(1)    NOT NULL COMMENT '삭제 여부',
    created_date    datetime      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일시',
    modified_date   datetime               DEFAULT NULL COMMENT '최종 수정일시',
    PRIMARY KEY (id)
    ) COMMENT '댓글';

CREATE TABLE IF NOT EXISTS tb_file (
    id              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '파일 번호 (PK)',
    post_id         bigint(20) NOT NULL COMMENT '게시글 번호 (FK)',
    original_name   varchar(255) NOT NULL COMMENT '원본 파일명',
    save_name       varchar(40) NOT NULL COMMENT '저장 파일명',
    size            int(11) NOT NULL COMMENT '파일 크기',
    delete_yn       tinyint(1) NOT NULL COMMENT '삭제 여부',
    created_date    datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
    deleted_date    datetime DEFAULT NULL COMMENT '삭제일시',
    PRIMARY KEY (id),
    KEY fk_post_file (post_id),
    CONSTRAINT fk_post_file FOREIGN KEY (post_id) REFERENCES tb_post (id)
    ) COMMENT '파일';

CREATE TABLE IF NOT EXISTS tb_member (
    id              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '회원 번호 (PK)',
    login_id        varchar(20) NOT NULL COMMENT '로그인 ID',
    password        varchar(60) NOT NULL COMMENT '비밀번호',
    name            varchar(20) NOT NULL COMMENT '이름',
    gender          enum('M','F') NOT NULL COMMENT '성별',
    birthday        date NOT NULL comment '생년월일',
    delete_yn       tinyint(1) NOT NULL COMMENT '삭제 여부',
    created_date    datetime NOT NULL DEFAULT current_timestamp() COMMENT '생성일시',
    modified_date   datetime DEFAULT NULL COMMENT '최종 수정일시',
    PRIMARY KEY (id),
    UNIQUE KEY uix_member_login_id (login_id)
    ) COMMENT '회원';
