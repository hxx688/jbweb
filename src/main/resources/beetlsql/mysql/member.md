list
===
select l.*,(select id from tfw_user where id = l.agent_id) user_id,(select real_name from lf_member where id=l.first_parent) first_name, 0 from lf_member l where 1=1

listAgent
===
select l.*,(select max(id) from tfw_user where member_id=l.id) user_id,
(SELECT count(0) from lf_member where agent_id = l.agent_id and is_agent = 0) subct, 
(SELECT count(0) from lf_member where parent_id = l.id and is_agent = 1) agentct, 
(select real_name from lf_member where id=l.first_parent) first_name,
0 from lf_member l

findOne
===
select * from lf_member where id = #{id}