select count(*) from actor;

select count(*) from directors;

select count(distinct mid) 
from actor a, casts c 
where a.fname = 'Kevin' and a.lname = 'Bacon' and c.pid = a.id;