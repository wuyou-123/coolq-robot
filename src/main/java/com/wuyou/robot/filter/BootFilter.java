package com.wuyou.robot.filter;

import com.alibaba.fastjson.JSONObject;
import com.forte.component.forcoolqhttpapi.utils.JSONDataUtil;
import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.wuyou.utils.GlobalVariable;

/**
 * 获取现在机器人是否开机
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@DIYFilter("boot")
public class BootFilter implements Filterable {

	@Override
	public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
		JSONObject json = JSONDataUtil.toJSONObject(msgget.getOriginalData());
		Boolean ret = GlobalVariable.bootMap.get(json.getString("group_id"));
		if (ret == null) {
			return false;
		}
		return ret;
	}

}
