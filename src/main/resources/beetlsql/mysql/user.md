list
===
select *,0 from tfw_user

findOneMember
===
select * from tfw_user where member_id = #{member_id}

mysqllist
===
select u.*,
	d.name as SEXNAME,
	e.name as STATUSNAME,
	dept.simpleName as DEPTNAME,
	(select GROUP_CONCAT(NAME) from tfw_role where  INSTR(CONCAT(",",u.ROLEID,","),CONCAT(",",ID,","))>0) ROLENAME
from tfw_user u 
	left join (select num,name from tfw_dict where code=101) d on u.sex=d.num 
	left join (select num,name from tfw_dict where code=901) e on u.status=e.num 
	left join (select id,simpleName from tfw_dept) dept on u.deptId=dept.id