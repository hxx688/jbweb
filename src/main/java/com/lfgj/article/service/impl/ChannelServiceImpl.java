package com.lfgj.article.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;
import com.rrgy.core.base.service.BaseService;
import com.rrgy.core.plugins.dao.Md;
import com.rrgy.core.toolbox.Paras;
import com.lfgj.article.model.Channel;
import com.lfgj.article.service.ChannelService;

/**
 * Generated by Blade.
 * 2017-09-02 18:13:22
 */
@Service
public class ChannelServiceImpl extends BaseService<Channel> implements ChannelService{

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		boolean temp = updateBy("status = #{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findOne(Object id) {
		return Md.selectUnique("channel.findOne", Paras.create().set("id", id), Map.class);
	}

}
