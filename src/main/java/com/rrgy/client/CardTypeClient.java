package com.rrgy.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rrgy.common.iface.RequestAbs;
import com.rrgy.common.iface.ResultVo;
import com.rrgy.core.annotation.Client;
import com.rrgy.core.toolbox.kit.JsonKit;

/**
 * 获取证件类型
 * @author Administrator
 *
 */
@Service
@Client(name = "rrgy_2017_1.0_cardType")
public class CardTypeClient extends RequestAbs{

	@Override
	public String getResult() {
		ResultVo rv = new ResultVo();
		List<String> cards = new ArrayList<String>();
		cards.add("大陆身份证");
		cards.add("境外身份证");
		cards.add("境外护照");
		cards.add("境外回乡证");
		rv.setReturnCode("0");
		rv.setReturnParams(cards);
		rv.setReturnMsg("获取数据成功");
		return JsonKit.toJson(rv);
	}

}
