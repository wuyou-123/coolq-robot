package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_GAME_POKER_PLAY_CANT_PASS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		if(channel.hasAttr(GlobalVariable.ATTRIBUTE_KEY_NUMBER)){
			channel.attr(GlobalVariable.ATTRIBUTE_KEY_NUMBER).set(channel.attr(GlobalVariable.ATTRIBUTE_KEY_NUMBER).get()+1);
		}else{
			channel.attr(GlobalVariable.ATTRIBUTE_KEY_NUMBER).set(1);
		}
		SimplePrinter.sendNotice(qq, "不允许不出牌!");
		ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(GetQQUtils.getClient(channel), null);
//		pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT);
	}

}
