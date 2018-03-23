queryList
===
select t.id,t.tixianNum,t.yuanyin,t.userId,t.amount,t.status,t.add_time,t.shui,t.shouxufei,t.fangshi,t.yinhangka,t.dakuan_time,t.kahao,t.kahumobile,t.kaihuming,t.kaihuhang,t.sheng,t.shi,t.zhihang,
u.mobile,u.bank_name ,u.real_name ,u.bank_acount,u.idcart,t.sheng,t.shi,t.zhihang,u.amount useramount,
t.shijiamount
,0 from dt_tixianjilu t
left join lf_member u on u.id=t.userId where u.is_agent = 0

findOne
===
select * from dt_tixianjilu where id = #{id}