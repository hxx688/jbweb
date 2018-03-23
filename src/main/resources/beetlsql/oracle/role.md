list
===
select r.*,d.simpleName DEPTNAME,(select name from tfw_role where id=r.pId) PNAME from tfw_role r left join tfw_dept d on r.deptId=d.id

diy
===
select 0 as "id", 0 as "pId",'顶级' as "name",'true' as "open" from  dual 
union
select ID as "id", pId as "pId",name as "name",(case when (pId=0 or pId is null) then 'true' else 'false' end) as "open" from  TFW_ROLE