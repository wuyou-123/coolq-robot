package org.nico.ratel.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.utils.GlobalVariable;
import org.nico.noson.Noson;
import org.nico.noson.util.string.StringUtils;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.helper.MapHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerEventListener_CODE_GAME_POKER_PLAY_REDIRECT implements ServerEventListener{

	@Override
	public void call(Player player, String data) {
		Room room = GlobalVariable.getRoom(player.getRoomId());
		Map<String, Object> datas = new HashMap<>();
		if(StringUtils.isNotBlank(data)) {
			datas = Noson.parseMap(data);
		}
		
		List<Map<String, Object>> clientInfos = new ArrayList<>(3);
		for(Player client: room.getPlayerList()){
			if(!client.getId().equals(player.getId())){
				clientInfos.add(MapHelper.newInstance()
						.put("clientId", client.getId())
						.put("clientNickname", client.getNickname())
						.put("type", client.getType())
						.put("surplus", client.getPokers().size())
						.put("position", player.getPre().getId().equals(client.getId()) ? "上家" : "下家")
						.map());
			}
		}
		
		String result = MapHelper.newInstance()
				.put("pokers", player.getPokers())
				.put("lastSellPokers", datas.get("lastSellPokers"))
				.put("lastSellClientId", datas.get("lastSellClientId"))
				.put("clientInfos", clientInfos)
				.put("sellClientId", room.getCurrentSellClient())
				.put("sellClientNickname", GlobalVariable.LANDLORDS_PLAYER.get(room.getCurrentSellClient()).getNickname())
				.json();

//		ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_REDIRECT, result);
		ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(player, result);

	}

}
