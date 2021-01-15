package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.ClientType;
import com.wuyou.enums.RoomStatus;
import com.wuyou.landlords.entity.Room;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.helper.MapHelper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClientEventListener_CODE_GAME_OVER extends ClientEventListener{

	@Override
	public void call(Player player, String data) {
		String qq = player.getId();
		Map<String, Object> map = MapHelper.parser(data);
		Room room = GlobalVariable.getRoom(player.getRoomId());
		ClientType type = (ClientType) map.get("winnerType");
		List<Player> winners = room.getPlayerList().stream().filter(item->item.getType()==type).collect(Collectors.toList());
		String winnersName = winners.toString();
		SenderUtil.sendPrivateMsg(qq, "玩家 " + winnersName + "[" + type + "]" + "赢得了比赛");
		SenderUtil.sendGroupMsg(room.getId(), "玩家 " + winnersName + "[" + type + "]" + "赢得了比赛");
		room.setStatus(RoomStatus.STOPPED);
		room.getPlayerList().forEach(item->{
			GlobalVariable.USER_INPUT.remove(item.getId());
			item.setStatus(ClientStatus.NO_READY);
		});
		SenderUtil.sendPrivateMsg(qq, "比赛结束, 发送\"继续\"可重新开始");
	}

}
