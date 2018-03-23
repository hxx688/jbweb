list
===
select * from tfw_user 

oraclelist
===
select u.*,
	d.name as DIC_SEX,
	e.name as DIC_STATUS,
	r.DIC_ROLEID,
	dept.simpleName as DIC_DEPTID 
from tfw_user u 
	left join (select num,name from tfw_dict where code=101) d on u.sex=d.num 
	left join (select num,name from tfw_dict where code=901) e on u.status=e.num 
	left join (select ROLEID,wm_concat(TO_CHAR(r.NAME)) DIC_ROLEID from (SELECT ROLEID from tfw_USER where ROLEID is not null GROUP BY ROLEID ) u 
	left join tfw_ROLE r on INSTR(','||u.ROLEID||',', ','||r.ID||',')>0 group by u.ROLEID ) r on u.roleId=r.roleId 
	left join (select id,simpleName from tfw_dept) dept on u.deptId=dept.id