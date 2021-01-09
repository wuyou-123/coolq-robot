package org.nico.ratel.landlords.server.event;

import com.wuyou.utils.GlobalVariable;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.ServerContains;

public class ServerEventListener_CODE_CLIENT_NICKNAME_SET implements ServerEventListener{

	public static final int NICKNAME_MAX_LENGTH = 10;
	
	@Override
	public void call(ClientSide client, String nickname) {
		System.out.println("设置昵称: "+nickname);
		if (nickname.trim().length() > NICKNAME_MAX_LENGTH) {
			String result = MapHelper.newInstance().put("invalidLength", nickname.trim().length()).json();
//			ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_CLIENT_NICKNAME_SET, result);
			ClientEventListener.get(ClientEventCode.CODE_CLIENT_NICKNAME_SET).call(client.getChannel(), result);

		}else{
			System.out.println(ServerContains.CLIENT_SIDE_MAP.get(client.getId()+""));
			ServerContains.CLIENT_SIDE_MAP.get(client.getId()+"").setNickname(nickname);
			ServerContains.CLIENT_SIDE_MAP.put(nickname, ServerContains.CLIENT_SIDE_MAP.get(client.getId()+""));
//			ServerContains.CLIENT_SIDE_MAP.remove(client.getId()+"");
			GlobalVariable.CLIENT_ID_MAP.put(client.getId(), nickname);
//			ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_SHOW_OPTIONS, nickname);
		}
	}

}
