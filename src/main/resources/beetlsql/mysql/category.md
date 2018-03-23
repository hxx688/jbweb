list
===
select c.*,(select title from dt_channel where c.channel_id=id) channel_name,0 from dt_article_category c

findOne
===
select * from dt_article_category where id = #{id}