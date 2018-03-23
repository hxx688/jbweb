indextixian
===
select COALESCE(SUM(amount),0) from dt_tixianjilu where add_time like #{yesterday}
and (select is_agent from lf_member m where m.id= userId) = 0
@if(!isEmpty(agent_id)){
   and (select agent_id from lf_member m where m.id= userId) = #{agent_id}
@}

countOrders
===
select COALESCE(SUM(order_money),0) from dt_orders where buy_time like #{yesterday}
and (select is_agent from lf_member m where m.id= person_id) = 0
@if(!isEmpty(agent_id)){
   and (select agent_id from lf_member m where m.id= person_id) = #{agent_id}
@}

topOrders
===
select COALESCE(SUM(earn),0) earn,min(person_name) person_name from dt_orders where buy_time like #{yesterday} 
and (select is_agent from lf_member m where m.id= person_id) = 0
@if(!isEmpty(agent_id)){
   and (select agent_id from lf_member m where m.id= person_id) = #{agent_id}
@}
group by person_id ORDER BY COALESCE(SUM(earn),0) desc LIMIT 0,3

bottomOrders
===
select COALESCE(SUM(earn),0) earn,min(person_name) person_name from dt_orders where buy_time like #{yesterday} 
and (select is_agent from lf_member m where m.id= person_id) = 0
@if(!isEmpty(agent_id)){
   and (select agent_id from lf_member m where m.id= person_id) = #{agent_id}
@}
group by person_id ORDER BY COALESCE(SUM(earn),0) asc LIMIT 0,3

index_change
===
select COALESCE(SUM(xin),0) from dt_aixinjilu where isadd = 2 and add_time like #{yesterday}

countAngel
===
select count(0) from dt_users where group_id = 1 and status in(0,3)

countAngelBuy
===
select count(0) from dt_xiaofeijilu where leixing = 1;

countSales
===
select count(0) from dt_users where group_id = 2 and status in(0,3)

shopOrder
===
select COALESCE(SUM(order_amount),0) from dt_orders where bili = #{bili} and payment_status=2 and payment_time like #{yesterday}

indexHeart
===
select COALESCE(SUM(xin),0) from dt_aixinjilu where isadd = 2 and bili = #{bili} and add_time like #{yesterday}

indexHeartTotal
===
select COALESCE(sum(xin),0) from dt_aixinjilu where leixing=#{leixing} and isadd=1 and bili=#{bili} and add_time like #{yesterday}

shopBean
===
select COALESCE(SUM(yifadou),0) from dt_orders where bili = #{bili} and payment_status=2 and payment_time like #{yesterday}

getHeartByDate
===
select COALESCE(SUM(xin),0) from dt_aixinjilu where leixing = #{leixing} and isadd = #{isadd} and bili = #{bili} and add_time like #{yesterday}

getAllAngelBean
===
select COALESCE(SUM(shuliang),0) from dt_fafangjilu where bili in (#{bili}) and status = #{status} and time like #{yesterday}
