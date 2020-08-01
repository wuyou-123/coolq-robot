package com.wuyou.robot.filter;

import com.forte.qqrobot.anno.DIYFilter;
import com.forte.qqrobot.anno.data.Filter;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.messages.msgget.MsgGet;
import com.forte.qqrobot.beans.types.CQCodeTypes;
import com.forte.qqrobot.listener.Filterable;
import com.forte.qqrobot.listener.ListenContext;
import com.forte.qqrobot.listener.invoker.AtDetection;
import com.forte.qqrobot.utils.CQCodeUtil;
import com.wuyou.utils.CQ;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取现在机器人是否开机
 * 
 * @author Administrator<br>
 *         2020年5月2日
 *
 */
@Beans
public class SetuFilter {
	@DIYFilter("setuImage")
	public class SetuImage implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			String message = msgget.getMsg();
			List<CQCode> list = CQCodeUtil.build().getCQCodeFromMsgByType(message, CQCodeTypes.image);
			for (CQCode CQCode : list) {
				return CQCode.getParam("file").equals("463FBD38F6A0BD4ED008D84D26DCE538.gif");
			}
			return false;

		}
	}

	@DIYFilter("setu")
	public class Setu implements Filterable {

		@Override
		public boolean filter(Filter filter, MsgGet msgget, AtDetection at, ListenContext context) {
			List<String> list = new ArrayList<String>();
			list.add("r18色图");
			list.add("r18涩图");
			list.add("来点r18色图");
			list.add("来点r18涩图");
			list.add("来一份r18色图");
			list.add("来一份r18涩图");
			list.add("来份r18色图");
			list.add("来份r18涩图");
			list.add("色图");
			list.add("涩图");
			list.add("来点色图");
			list.add("来点涩图");
			list.add("来一份色图");
			list.add("来一份涩图");
			list.add("来份色图");
			list.add("来份涩图");
			list.add("来点好看的");
			list.add("来点好康的");
			String message = msgget.getMsg();
			String atThis = CQ.at(msgget.getThisCode());
			if (list.contains(message.replace(atThis, "").trim().toLowerCase()))
				return true;
			if(message.contains("色")&&message.contains("图")&&message.length()<10)
				return true;
			return false;
		}
	}
}
