package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.server.ServerContains;

public class ClientEventListener_CODE_CLIENT_CONNECT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String longId = channel.id().asLongText();
		Integer clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
		if (null == clientId) {
			clientId = ServerContains.getClientId();
			ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
		}
		SimpleClient.id = Integer.parseInt(data);
	}

}
