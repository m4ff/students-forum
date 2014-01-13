DROP TABLE "user";
DROP TABLE "group";
DROP TABLE "user_group";
DROP TABLE "post";
DROP TABLE "file";

CREATE TABLE "user" (
user_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
user_name VARCHAR(50),
user_password VARCHAR(50),
user_moderator BOOLEAN,
PRIMARY KEY (user_id),
UNIQUE (user_name)
);

CREATE TABLE "group" (
group_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
group_name VARCHAR(50),
creator_id INTEGER,
PRIMARY KEY (group_id),
UNIQUE (group_name)
);

CREATE TABLE "post" (
post_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
user_id INTEGER,
group_id INTEGER,
post_text VARCHAR(32672),
post_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (post_id)
);
CREATE INDEX post_date_index ON "post"(post_date);

CREATE TABLE "user_group" (
user_id INTEGER,
group_id INTEGER,
group_accepted BOOLEAN,
visible BOOLEAN,
PRIMARY KEY (user_id, group_id)
);

CREATE TABLE "file" (
post_id INTEGER,
file_name VARCHAR(50),
file_mime VARCHAR(50),
file_size INTEGER,
PRIMARY KEY (post_id, file_name)
);

CREATE INDEX post_date_index ON NAN."post"(post_date);

