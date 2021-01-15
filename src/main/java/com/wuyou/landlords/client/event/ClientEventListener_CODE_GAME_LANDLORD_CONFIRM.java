package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.helper.MapHelper;
import com.wuyou.landlords.server.event.ServerEventListener;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_CONFIRM extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		Map<String, Object> map = MapHelper.parser(data);
		
		String landlordNickname = String.valueOf(map.get("landlordNickname"));
		
		SenderUtil.sendPrivateMsg(qq, landlordNickname + " 已经成为地主并获得了额外的三张牌");
		
		List<Poker> additionalPokers = Noson.convert(map.get("additionalPokers"), new NoType<List<Poker>>() {});
		SenderUtil.sendPrivateMsg(qq, CQ.getPoker(additionalPokers));
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(player,null);
//		pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
