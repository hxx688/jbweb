list
===
*where 1=1
*and (select u.user_name from dt_users u where u.id=t.userId) like CONCAT(CONCAT('%', #{user_name_skip}),'%')
select t.id,t.userId,t.shuliang,t.time,t.orderId,t.bili,t.status,
(select u.nick_name from dt_users u where u.id=t.userId) as user_name,
(select a.title from dt_youhuizubie a where a.id=t.bili) as bili_name
,0 from dt_fafangjilu t


findOne
===
select * from dt_fafangjilu where id = #{id}

listAll
===
select *,0 from dt_fafangjilu

getDouTotal
===
select COALESCE(SUM(shuliang),0) from dt_fafangjilu where 1=1
@if(!isEmpty(userId)){
   and userId = #{userId}
@}
@if(!isEmpty(bili)){
   and bili = #{bili}
@}
@if(!isEmpty(status)){
   and status = #{status}
@}
@if(!isEmpty(time)){
   and time like #{time}
@}