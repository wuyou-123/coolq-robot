package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.server.ServerContains;

public class ClientEventListener_CODE_CLIENT_CONNECT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String longId = channel.remoteAddress().toString();
		//			clientId = ServerContains.getClientId();
		ServerContains.CHANNEL_ID_MAP.putIfAbsent(longId, data);
		SimpleClient.id = data;
	}

}
