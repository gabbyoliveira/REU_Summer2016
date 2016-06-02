use producthunt;
select user_username, sum(votes_count) as total_votes
from posts
group by user_username;


use producthunt;
select username, followers_count
from users;


select user_username, SUM(votes_count) as total_votes
into outfile '/user_votes.csv'
CHARACTER SET UTF8MB4 FIELDS TERMINATED BY ';'
	ENCLOSED BY '"'  
	ESCAPED BY '' 
LINES TERMINATED BY '\n'
from posts
group by user_username;


select username, followers_count
into outfile '/user_followers.csv'
CHARACTER SET UTF8MB4 FIELDS TERMINATED BY ';'
	ENCLOSED BY '"'  
	ESCAPED BY '' 
LINES TERMINATED BY '\n'
from posts;


use producthunt;
select count(followers), count(votes)
from mergeddata
where votes > 0 and (followers > 0 and followers <= 500);