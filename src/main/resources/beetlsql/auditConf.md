list
===
select t.id,t.user_id,t.model_type,t.add_user,t.add_time,t.audit_level,
(select a.name from tfw_user a where t.user_id=a.id) as user_name,
(select b.name from tfw_user b where t.add_user=b.id) as add_user_name
,0 from dt_audit_conf t

findOne
===
select * from dt_audit_conf where id = #{id}