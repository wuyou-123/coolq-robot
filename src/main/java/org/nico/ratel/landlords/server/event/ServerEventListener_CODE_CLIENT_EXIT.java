package org.nico.ratel.landlords.server.event;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import com.wuyou.entity.Player;
import org.nico.ratel.landlords.entity.Room;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.enums.ClientRole;
import org.nico.ratel.landlords.helper.MapHelper;

public class ServerEventListener_CODE_CLIENT_EXIT implements ServerEventListener{

	@Override
	public void call(Player player, String data) {

		Room room = GlobalVariable.getRoom(player.getRoomId());
		System.out.println("退出clientId: "+player.getId());
		if(room != null) {
			String result = MapHelper.newInstance()
								.put("roomId", room.getId())
								.put("exitClientId", player.getId())
								.put("exitClientNickname", player.getNickname())
								.json();
			for(Player client: room.getPlayerList()) {
				if(client.getRole() == ClientRole.PLAYER){
					ClientEventListener.get(ClientEventCode.CODE_CLIENT_EXIT).call(client, result);
					client.init();
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SenderUtil.sendGroupMsg(room.getId(), player.getId()+"离开了房间, 房间解散");

//			notifyWatcherClientExit(room, player);

			GlobalVariable.removeRoom(room.getId());
			GlobalVariable.LANDLORDS_ROOM.remove(room.getId());
		}
	}

	/**
	 * 通知所有观战者玩家退出游戏
	 *
	 * @param room 房间
	 * @param player 退出游戏玩家
	 */
	private void notifyWatcherClientExit(Room room, Player player) {
		for (Player watcher : room.getWatcherList()) {
//			ChannelUtils.pushToClient(watcher, ClientEventCode.CODE_CLIENT_EXIT, player.getNickname());
			ClientEventListener.get(ClientEventCode.CODE_CLIENT_EXIT).call(watcher, player.getNickname());

		}
	}
}
