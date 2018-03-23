list
===
select ac.*,(select title from dt_article_category where id=ac.category_id) category_title
,(select title from dt_channel where id=ac.channel_id) channel_title,0 from dt_article ac

findOne
===
select * from dt_article where id = #{id}