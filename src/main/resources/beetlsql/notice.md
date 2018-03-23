list
===
select t.*, d.name DIC_F_IT_LX,u.name DIC_F_IT_CJR  from  tb_yw_tzgg t 
left join (select num,name from tfw_dict where code=102)d on t.f_it_lx=d.num
left join (select * from tfw_user where status = 1) u on t.f_it_cjr = u.id
where t.F_IT_LX != 2 ORDER by F_DT_FBSJ desc
      
list_protocol
===
select t.*, d.name DIC_F_IT_LX,u.name DIC_F_IT_CJR  from  tb_yw_tzgg t 
left join (select num,name from tfw_dict where code=102)d on t.f_it_lx=d.num
left join (select * from tfw_user where status = 1) u on t.f_it_cjr = u.id
where t.F_IT_LX = 2
  		
data
===
select t.F_IT_XL,t.F_VC_BT,t.F_IT_LX,t.F_TX_NR,t.F_DT_FBSJ,t.F_DT_CJSJ,t.F_IT_CJR,t.F_IT_TP,t.VERSION version,
		d.name DIC_F_IT_LX 
	from 
		tb_yw_tzgg t 
		left join (select num,name from tfw_dict where code=102)d on t.f_it_lx=d.num 
		left join tfw_attach a on t.F_IT_TP=a.ID
where t.F_IT_XL = #{id}
		
update
===
 update tb_yw_tzgg 
  set
	 @if(!isEmpty(f_it_cjr)){
		f_it_cjr = #{f_it_cjr},
	 @} if(!isEmpty(f_it_lx)){
		f_it_lx = #{f_it_lx},
	 @} if(!isEmpty(f_it_tp)){
		f_it_tp = #{f_it_tp},
	 @} if(!isEmpty(f_tx_nr)){
		f_tx_nr = #{f_tx_nr},
	 @} if(!isEmpty(f_vc_bt)){
		f_vc_bt = #{f_vc_bt},
	 @} if(!isEmpty(f_dt_cjsj)){
		f_dt_cjsj = #{f_dt_cjsj},
	 @} if(!isEmpty(f_dt_fbsj)){
		f_dt_fbsj = #{f_dt_fbsj},
	 @} if(!isEmpty(version)){
		version = #{version}
	 @}
 where f_it_xl = #{f_it_xl}

findById
===
select * from tb_yw_tzgg where f_it_xl = #{id}


diy
===
select NUM as "num",
	ID as "id",
	PID as "pId",
	NAME as "name",
	(case when (pId=0 or pId is null) then 'true' else 'false' end) "open" 
from  TFW_DICT
where CODE = 102 and num != 2

diy_protocol
===
select NUM as "num",
	ID as "id",
	PID as "pId",
	NAME as "name",
	(case when (pId=0 or pId is null) then 'true' else 'false' end) "open" 
from  TFW_DICT
where CODE = 102 and num = 2

list_first
===
select * from tb_yw_tzgg where F_IT_LX !=2 ORDER BY F_DT_CJSJ desc