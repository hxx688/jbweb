list
===
select *,0 from dt_users

findOne
===
select * from dt_users where id = #{id}

listYongjin
===
SELECT
	l.*, ifnull(
		(
			SELECT
				sum(f.amount)
			FROM
				dt_financial f,
				lf_member m
			WHERE
				f.user_id = m.id
			AND f.source_type = '充值'
			AND m.is_agent = 0
			AND 
			CASE l.is_agent WHEN 1 THEN find_in_set(l.id, m.parent_ids) ELSE f.user_id = l.id END
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(create_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(create_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS recharge_total,
	ifnull(
		(
			SELECT
				sum(t.shijiamount)
			FROM
				dt_tixianjilu t,
				lf_member m
			WHERE
				t.userId = m.id
			AND t. STATUS = 2
			AND 
			CASE l.is_agent WHEN 1 THEN find_in_set(l.id, m.parent_ids) ELSE t.userId=l.id END
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(t.dakuan_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(t.dakuan_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS withdraw_total,
	ifnull(
		(
			SELECT
				SUM(t.earn)
			FROM
				dt_orders t,
				lf_member u
			WHERE
				t.person_id = u.id
			AND t.is_agent = 0
			AND 
			CASE l.is_agent WHEN 1 THEN find_in_set(l.id, u.parent_ids) ELSE t.person_id = l.id END
			AND t. STATUS = 2
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS profi_loss_total,
	ifnull(
		(
			SELECT
				SUM(t.fee)
			FROM
				dt_orders t,
				lf_member u
			WHERE
				t.person_id = u.id
			AND t.is_agent = 0
		AND 
			CASE l.is_agent WHEN 1 THEN find_in_set(l.id, u.parent_ids) ELSE t.person_id = l.id END
			AND t. STATUS = 2
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS sxf_total,
	0 FROM
	lf_member l
	
rechargeTotal
===
SELECT
	l.*, 
ifnull(
		(
			SELECT
				sum(f.amount)
			FROM
				dt_financial f,
				lf_member m
			WHERE f.user_id = m.id
				and m.is_agent = 0
				and CASE l.is_agent WHEN 1 THEN find_in_set(l.id, m.parent_ids) ELSE f.user_id=l.id END
			AND f.source_type = '充值'
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(create_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(create_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS total_result,
		0 from
	lf_member l
	
withdrawTotal
===
SELECT
	l.*, 
ifnull(
		(
			SELECT
				sum(t.shijiamount)
			FROM
				dt_tixianjilu t,
				lf_member m
			WHERE t.userId = m.id
				and t.status = 2
				and CASE l.is_agent WHEN 1 THEN find_in_set(l.id, m.parent_ids) ELSE t.userId = l.id END
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(t.dakuan_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(t.dakuan_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS total_result,
		0 from
	lf_member l

profiLossTotal
===
SELECT
	l.*, 
ifnull(
		(
			SELECT SUM(t.earn)
		FROM dt_orders t,lf_member u
		WHERE t.person_id = u.id
			AND t.is_agent = 0
			and CASE l.is_agent WHEN 1 THEN find_in_set(l.id, u.parent_ids) ELSE t.person_id = l.id END
			AND t. STATUS = 2
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS total_result,
		0 from
	lf_member l

sxfTotal
===
SELECT
	l.*, 
ifnull(
		(
			SELECT SUM(t.fee)
		FROM dt_orders t,lf_member u
		WHERE t.person_id = u.id
			AND t.is_agent = 0
			and CASE l.is_agent WHEN 1 THEN find_in_set(l.id, u.parent_ids) ELSE t.person_id = l.id END
			AND t. STATUS = 2
			@if(!isEmpty(create_time_gt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') >= #{create_time_gt_skip}
			@}
			@if(!isEmpty(create_time_lt_skip)){
			   and DATE_FORMAT(t.sale_time,'%Y-%m-%d') <= #{create_time_lt_skip}
			@}
		),
		0
	) AS total_result,
		0 from
	lf_member l
	
	
	
	