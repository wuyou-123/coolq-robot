package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_CONFIRM extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		
		String landlordNickname = String.valueOf(map.get("landlordNickname"));
		
		SimplePrinter.sendNotice(qq, landlordNickname + " 已经成为地主并获得了额外的三张牌");
		
		List<Poker> additionalPokers = Noson.convert(map.get("additionalPokers"), new NoType<List<Poker>>() {});
		SimplePrinter.printPokers(qq, additionalPokers);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(GetQQUtils.getClient(channel),null);
//		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
