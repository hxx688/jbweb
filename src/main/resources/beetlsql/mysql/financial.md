list
===
select f.*,u.is_agent,u.agent_name, 0 from dt_financial f,lf_member u where f.user_id=u.id 

listUser
===
select f.*,u.is_agent,u.agent_name, 0 from dt_financial f,lf_member u where f.user_id=u.id

findOne
===
select * from dt_financial where id = #{id}