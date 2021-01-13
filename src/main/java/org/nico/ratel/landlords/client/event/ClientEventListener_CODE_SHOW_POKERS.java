package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.helper.MapHelper;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_SHOW_POKERS extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		
		Map<String, Object> map = MapHelper.parser(data);
		
		lastSellClientNickname = (String) map.get("clientNickname");
		lastSellClientType = (String) map.get("clientType");
		
		SenderUtil.sendPrivateMsg(qq, lastSellClientNickname + "[" + lastSellClientType + "] 出牌:");
		lastPokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});
		SenderUtil.sendPrivateMsg(qq, CQ.getPoker(lastPokers));

		if(map.containsKey("sellClientNickname")) {
			SenderUtil.sendPrivateMsg(qq, "下一位玩家是 " + map.get("sellClientNickname") + ", 请等待他确认.");
		}
	}

}
