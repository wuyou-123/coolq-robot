package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.helper.MapHelper;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_OVER extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		SimplePrinter.sendNotice(qq, "玩家 " + map.get("winnerNickname") + "[" + map.get("winnerType") + "]" + "赢得了比赛");
		SimplePrinter.sendNotice(qq, "比赛结束, 友谊第一, 比赛第二");
	}

}
