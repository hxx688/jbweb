list
===
select t.id,t.user_id,t.model_id,t.model_type,t.add_time,t.audit_status,t.audit_remark,t.audit_level,
(select a.name from tfw_user a where t.user_id=a.id) as user_name
,0 from dt_audit_log t where t.model_id = #{model_id} and t.model_type = #{model_type} 

findOne
===
select * from dt_audit_log where id = #{id}