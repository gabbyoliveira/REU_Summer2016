
DROP TABLE IF EXISTS Posts;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Topics;
DROP TABLE IF EXISTS Topics_Posts;
DROP TABLE IF EXISTS Collections;
DROP TABLE IF EXISTS Votes;
DROP TABLE IF EXISTS MergedData;
DROP TABLE IF EXISTS Merged_Data;

CREATE TABLE Posts
(
	id int, 
	created_at TIMESTAMP, 
	name varchar(255), 
	tagline varchar(1000), 
	user_id int, 
	user_username varchar(255), 
	votes_count int, 
	comments_count int, 
	redirect_url varchar(500), 
	discussion_url varchar(500)
);

CREATE TABLE Users
(
	id int, 
	created_at TIMESTAMP, 
	name varchar(255), 
	username varchar(255),
	headline varchar(1000), 
	invited_by_id int, 
	followers_count int,
	followings_count int,
	votes_count int, 
	posts_count int,
	maker_of_count int,
	comments_count int, 
	profile_url varchar(500)
);

CREATE TABLE Topics
(
	id int, 
	created_at TIMESTAMP, 
	name varchar(255), 
	tagline varchar(1000),  
	followers_count int, 
	posts_count int
);

CREATE TABLE Topics_Posts
(
	topic_id int, 
	post_id int
);

CREATE TABLE Collections
(
	id int, 
	created_at TIMESTAMP, 
	featured_at int, 
	name varchar(255), 
	tagline varchar(255), 
	subscriber_count int, 
	user_id int, 
	user_username varchar(255), 
	posts_count int
);

CREATE TABLE Votes
(
	id int, 
	created_at TIMESTAMP, 
	user_id varchar(255),
	post_id int, 
	user_username varchar(255), 
	post_name varchar(255),
	post_tagline varchar(255), 
	post_discussion_url varchar(500)
);

CREATE TABLE MergedData
(
	username varchar(255),
    followers int,
    votes int
);

-- 57243;2016-03-31 19:55:33.127923-07;We Are Heroes;The next-gen MOBA+ RPG for mobile devices;443077;anh;1;0;http://www.producthunt.com/l/b3443c7ac79b50;http://www.producthunt.com/posts/we-are-heroes-2
-- 57242;2016-03-31 19:53:36.614695-07;Tesla Model 3;The mass market electric car by Tesla is here ⚡️🚘;16056;erictwillis;44;18;http://www.producthunt.com/l/3843d5a8440dc8;http://www.producthunt.com/posts/tesla-model-3-2

LOAD DATA LOCAL INFILE 'data/posts--2016-05-09_16-47-31-UTC.csv'
INTO TABLE Posts
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ';'
    ENCLOSED BY '"'
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(id, created_at, name, tagline, user_id, user_username, votes_count, comments_count, redirect_url, discussion_url);
SHOW warnings;


LOAD DATA LOCAL INFILE 'data/topics--2016-05-09_16-52-16-UTC.csv'
INTO TABLE Topics
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ';'
    ENCLOSED BY '"'
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(id, created_at, name, tagline, followers_count, posts_count);
SHOW warnings;


LOAD DATA LOCAL INFILE 'data/topic_posts_associations--2016-05-09_16-52-16-UTC.csv'
INTO TABLE Topics_Posts
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ';'
    ENCLOSED BY '"'
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(topic_id, post_id);
SHOW warnings;


LOAD DATA LOCAL INFILE 'data/collections--2016-05-09_16-51-58-UTC.csv'
INTO TABLE Collections
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ';'
    ENCLOSED BY '"'
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(id, created_at, featured_at, name, tagline, subscriber_count, user_id, user_username, posts_count);
SHOW warnings;

LOAD DATA LOCAL INFILE 'data/votes--2016-05-09_16-48-43-UTC.csv'
INTO TABLE Votes
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ';'
    ENCLOSED BY '"'
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(id, created_at, user_id, post_id, user_username, post_name, post_tagline, post_discussion_url);
SHOW warnings;


LOAD DATA LOCAL INFILE 'data/users--2016-05-09_16-47-34-UTC.csv'
INTO TABLE Users
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ';'
    ENCLOSED BY '"'
	ESCAPED BY ''
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(id, created_at, name, username, headline, invited_by_id, followers_count, 
followings_count, votes_count, posts_count, maker_of_count, comments_count, profile_url);
SHOW warnings;

LOAD DATA LOCAL INFILE 'data/merged_data_nozerovotes.csv'
INTO TABLE MergedData
CHARACTER SET UTF8MB4
FIELDS TERMINATED BY ','
    ENCLOSED BY '"'
	ESCAPED BY ''
-- LINES TERMINATED BY '\r\n'
LINES TERMINATED BY '\n'
IGNORE 1 LINES -- Skip header
(username, followers, votes);
SHOW warnings;