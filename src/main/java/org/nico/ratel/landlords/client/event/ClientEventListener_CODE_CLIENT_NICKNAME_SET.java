package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ClientEventListener_CODE_CLIENT_NICKNAME_SET extends ClientEventListener{

	public static final int NICKNAME_MAX_LENGTH = 15;

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
//		String nickname = SimpleWriter.write("nickname");

		pushToServer(channel, ServerEventCode.CODE_CLIENT_NICKNAME_SET, qq);
		pushToServer(channel, ServerEventCode.CODE_SEND_TO_QQ, "欢迎来到斗地主");

		SimplePrinter.sendNotice(GetQQUtils.getQQ(channel),"欢迎来到斗地主");
	}



}
