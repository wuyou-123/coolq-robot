package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.helper.MapHelper;

import java.util.Map;

public class ClientEventListener_CODE_CLIENT_EXIT extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		System.out.println(qq+"掉线了");
		Map<String, Object> map = MapHelper.parser(data);
		GlobalVariable.LANDLORDS_PLAYER.remove(qq);
//		GlobalVariable.LANDLORDS_ROOM.remove()
		String exitClientId = (String) map.get("exitClientId");
		String role;
		if(exitClientId.equals(qq)) {
			role = "你";
		}else {
			role = String.valueOf(map.get("exitClientNickname"));
		}
		SenderUtil.sendPrivateMsg(qq, role + " 离开了房间, 房间解散!");

		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(player, data);
	}

}
