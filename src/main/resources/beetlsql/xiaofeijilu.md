list
===
select *,0 from dt_xiaofeijilu

findOne
===
select * from dt_xiaofeijilu where id = #{id}

getAixinTotal
===
select  COALESCE((SUM(t.xinshu)-SUM(t.fanhuanxin)),0) as aixinTotal from (select * from dt_xiaofeijilu GROUP BY userId,bili) t where t.leixing = #{type}
@if(!isEmpty(biliId)){
   and t.bili = #{biliId}
@}
@if(!isEmpty(userId)){
   and t.userId = #{userId}
@}

getListByBili
===
select * from dt_xiaofeijilu t where t.leixing=#{type} and t.bili=#{biliId} GROUP BY t.userId

getAixinAllTotal
===
select COALESCE(SUM(t.xinshu),0) as aixinTotal from (select * from dt_xiaofeijilu GROUP BY userId,bili) t where 1=1 
	@if(!isEmpty(bili)){
		and t.bili = #{bili} 
	@}
	@if(!isEmpty(leixing)){
		and t.leixing = #{leixing} 
	@}
 and t.userId = #{userId}
 
getFanhuanxinAllTotal
===
select COALESCE(SUM(t.fanhuanxin),0) as aixinTotal from (select * from dt_xiaofeijilu GROUP BY userId,bili) t where 1=1 
	@if(!isEmpty(bili)){
		and t.bili = #{bili} 
	@}
	@if(!isEmpty(leixing)){
		and t.leixing = #{leixing} 
	@}
 and t.userId = #{userId}

getGroupList
===
select * from dt_xiaofeijilu t GROUP BY t.userId,t.leixing,t.bili