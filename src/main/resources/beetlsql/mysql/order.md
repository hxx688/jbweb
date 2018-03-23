list
===
select t.*, u.real_name as user_name,u.mobile
,0 from dt_orders t,lf_member u where u.id=t.person_id and u.is_agent=0

list2
===
select t.*, u.real_name as user_name,u.mobile
,0 from dt_orders t,lf_member u where u.id=t.person_id and u.is_agent=0

listUser
===
select t.*, u.real_name as user_name,u.mobile
,0 from dt_orders t,lf_member u where u.id=t.person_id

findOne
===
select * from dt_orders where id = #{id}

listSale
===
select t.*,0 from dt_orders t where t.person_id = #{id}
and status = #{status}

listAll
===
select t.*,0 from dt_orders t,lf_member u where u.id=t.person_id and t.status = #{status}
