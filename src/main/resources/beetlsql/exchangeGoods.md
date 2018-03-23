list
===
select *,0 from exchange_goods where status in (0,1)

findOne
===
select * from exchange_goods where id = #{id}