list
===
select ps.*,p.product_name,u.name creator_name,0 from dt_product_sale ps
INNER join dt_product p on p.id=ps.product_id
INNER join (select * from tfw_user where status = 1) u on p.creator = u.id

findOne
===
select * from dt_product_sale where id = #{id}