list
===
select t.*,0 from dt_users t

findOne
===
select *,0 from dt_users where id = #{id}

listSales
===
select t.*,(select  COALESCE((SUM(xf.xinshu)-SUM(xf.fanhuanxin)),0)
 from (select * from dt_xiaofeijilu GROUP BY userId,bili) xf where xf.userId=t.id) as aixin,0 from dt_users t where group_id = 2 and status in(0,2,3)

listSalesApply
===
select t.*,0 from dt_users t where group_id = 2 and status = 2

listPartner
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where group_id = 3 and status in(0,2,3)

listPartnerApply
===
select t.*,0 from dt_users t where group_id = 3 and status = 2

listSilver
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where group_id = 4 and status in(0,2,3)

listSilverApply
===
select t.*,0 from dt_users t where group_id = 4 and status = 2

listGold
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where group_id = 5 and status in(0,2,3)

listGoldApply
===
select t.*,0 from dt_users t where group_id = 5 and status = 2

listProvince
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where group_id = 7 and status in(0,2,3)

listProvinceApply
===
select t.*,0 from dt_users t where group_id = 7 and status = 2

listCity
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where group_id = 6 and status in(0,2,3)

listCityApply
===
select t.*,0 from dt_users t where group_id = 6 and status = 2

listStone
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where group_id = 10 and status in(0,2,3)

listStoneApply
===
select t.*,0 from dt_users t where group_id = 10 and status = 2

listAll
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where status in (0,3) and FIND_IN_SET(#{bili_skip},bili)

updateStatus
===
update dt_users set status = #{status},audit_level = #{audit_level} where id = #{id}

listHhr
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where status in(0,3)

getSaleCount
===
select count(0) from dt_users where group_id = #{group_id} and status in(0,3) and shangjilist like #{shangjilist}

getSumAmount
===
select COALESCE(SUM(order_amount),0) from dt_orders where payment_status = #{payment_status} and maijiaId in(select id from dt_users where group_id = #{group_id} and shangjilist like #{shangjiId} and bili = #{bili}
@if(!isEmpty(reg_time)){
   and reg_time > #{reg_time}
@}
)

listHhrOrder
===
select *,(select COALESCE(SUM(order_amount),0) from dt_orders where payment_status =2 and maijiaId = ur.id and bili = #{bili_skip}) sale_money,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where status in(0,3)

listRecommend
===
select *,(select title from dt_user_groups where id=ur.group_id) group_name,0 from dt_users ur where status in (0,3)


getAddressList
===
select *,0 from dt_delivery_address
