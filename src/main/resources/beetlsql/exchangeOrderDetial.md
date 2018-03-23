list
===
select *,0 from exchange_order_detail where order_number = #{order_number}

findOne
===
select * from exchange_order_detail where id = #{id}