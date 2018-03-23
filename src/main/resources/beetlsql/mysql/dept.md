list
===
select d.*,(select simpleName from tfw_dept  where id=d.pId) PNAME from tfw_dept d 

diy
===
select 0 as "id", 0 as "pId",'顶级' as "name",'true' as "open" from  dual 
union
select ID as "id", pId as "pId",simplename as "name",'false' as "open" from  TFW_DEPT