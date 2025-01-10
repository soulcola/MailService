--liquibase formatted sql

--changeset gkislin:1
CREATE SEQUENCE common_seq START 100000;

CREATE TABLE city
(
    ref  TEXT PRIMARY KEY,
    name TEXT NOT NULL
);

ALTER TABLE users
    ADD COLUMN city_ref TEXT REFERENCES city (ref) ON UPDATE CASCADE;

--changeset gkislin:2
CREATE TABLE project
(
    id          INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    name        TEXT UNIQUE NOT NULL,
    description TEXT
);

CREATE TYPE GROUP_TYPE AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE TABLE groups
(
    id         INTEGER PRIMARY KEY DEFAULT nextval('common_seq'),
    name       TEXT UNIQUE NOT NULL,
    type       GROUP_TYPE  NOT NULL,
    project_id INTEGER     NOT NULL REFERENCES project (id)
);

CREATE TABLE user_group
(
    user_id  INTEGER NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    group_id INTEGER NOT NULL REFERENCES groups (id),
    CONSTRAINT users_group_idx UNIQUE (user_id, group_id)
);

--changeset petrtitov:3
CREATE SEQUENCE mail_seq START 100000;
CREATE TYPE send_status AS ENUM ('success', 'failed');
CREATE TABLE mail_hist
(
    id        INTEGER PRIMARY KEY DEFAULT nextval('mail_seq'),
    send_to   TEXT NOT NULL,
    copy_to   TEXT,
    send_from TEXT NOT NULL,
    subject   TEXT,
    body      TEXT,
    date      date,
    time      time,
    status    send_status
);

--changeset petrtitov:4
DROP table mail_hist;
DROP SEQUENCE mail_seq;
CREATE TABLE mail_hist
(
    id       SERIAL PRIMARY KEY,
    send_to  TEXT NULL,
    copy_to  TEXT NULL,
    subject  TEXT,
    body     TEXT,
    datetime TIMESTAMP NOT NULL,
    status   TEXT NOT NULL
);
COMMENT ON TABLE mail_hist IS 'История отправки email';
COMMENT ON COLUMN mail_hist.datetime IS 'Время отправки';