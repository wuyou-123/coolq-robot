package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.landlords.entity.Room;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.client.event.ClientEventListener;
import com.wuyou.landlords.helper.MapHelper;

public class ServerEventListener_CODE_GAME_POKER_PLAY_PASS implements ServerEventListener{

	@Override
	public void call(Player player, String data) {
		Room room = GlobalVariable.getRoom(player.getRoomId());

		if(room != null) {
			if(room.getCurrentSellClient().equals(player.getId())) {
				if(!player.getId().equals(room.getLastSellClient())) {
					Player turnClient = player.getNext();

					room.setCurrentSellClient(turnClient.getId());
					SenderUtil.sendGroupMsg(room.getId(), player.getNickname() + " 跳过了. 现在轮到" + turnClient.getNickname() + "出牌.");

					for(Player client: room.getPlayerList()) {
						String result = MapHelper.newInstance()
								.put("clientId", player.getId())
								.put("clientNickname", player.getNickname())
								.put("nextClientId", turnClient.getId())
								.put("nextClientNickname", turnClient.getNickname())
								.json();
///							ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_PASS, result);
							ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_PASS).call(client, result);
					}

//					notifyWatcherPlayPass(player);
				}else {
///					ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_CANT_PASS, null);
					ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_CANT_PASS).call(player, null);

				}
			}else {
///				ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR, null);
				ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR).call(player, null);

			}
		}
///		else {
///			ClientEventListener.get(ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST1).call(player, null);
///			ChannelUtils.pushToClient(player, ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
///		}
	}

	/**
	 * 通知观战者玩家不出牌
	 *
	 * @param player	不出牌的玩家
	 */
	private void notifyWatcherPlayPass(Player player) {
			ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_PASS).call(player, null);
	}
}
