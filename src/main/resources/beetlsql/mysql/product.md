list
===
select p.*,d.name group_name,u.name creator_name,0 from dt_product p 
INNER join (select num,name from tfw_dict where code=200)d on p.group_id=d.num
INNER join (select * from tfw_user where status = 1) u on p.creator = u.id
where p.status in (1,0) 

findOne
===
select * from dt_product where id = #{id}