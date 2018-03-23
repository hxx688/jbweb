list
===
select t.id,t.orderNum,t.userId,t.userId2,t.dou,t.add_time,t.status,
(select u1.user_name from dt_users u1 where u1.id=t.userId) as user_out,
(select u2.user_name from dt_users u2 where u2.id=t.userId2) as user_in
,0 from dt_zengsongjilu t

zhijuanlist
===
select t.id,t.orderNum,t.userId,t.userId2,t.dou,t.add_time,t.status,
(select u1.user_name from dt_users u1 where u1.id=t.userId) as user_out,
'公益基金会' as user_in
,0 from dt_zengsongjilu t where (t.userId2 = 0 or t.userId2 = 2)

findOne
===
select * from dt_zengsongjilu where id = #{id}

getDouTotal
===
select COALESCE(SUM(dou),0) from dt_zengsongjilu where 1=1
@if(!isEmpty(userId)){
   and userId = #{userId}
@}
@if(!isEmpty(userId2)){
   and userId2 = #{userId2}
@}
@if(!isEmpty(add_time)){
   and add_time > #{add_time}
@}
