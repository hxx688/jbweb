list
===
select o.*,u.real_name,u.mobile,(select l.real_name from lf_member l where l.id=u.first_parent) first_name,0 from dt_online_service o,lf_member u where o.send_id=u.id

findOne
===
select * from dt_online_service where id = #{id}