package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.helper.MapHelper;

import java.util.Map;

public class ClientEventListener_CODE_ROOM_JOIN_FAIL_BY_FULL extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		Map<String, Object> dataMap = MapHelper.parser(data);
//		pushToServer(player, ServerEventCode.CODE_SEND_TO_QQ, "加入房间失败! 房间 " + dataMap.get("roomId") + " 已满!");

		SenderUtil.sendPrivateMsg(qq, "加入房间失败! 房间 " + dataMap.get("roomId") + " 已满!");
//		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(player, data);
	}



}
