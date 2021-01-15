package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.enums.RoomStatus;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.landlords.entity.Room;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import com.wuyou.landlords.client.event.ClientEventListener;
import com.wuyou.landlords.helper.MapHelper;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServerEventListener_CODE_ROOM_JOIN implements ServerEventListener{

	@Override
	public void call(Player player, String data) {

		Room room = GlobalVariable.getRoom(data);
		System.out.println(player.getId()+"尝试加入房间");
		if(room == null) {
			String result = MapHelper.newInstance()
								.put("roomId", data)
								.json();
			ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_INEXIST).call(player, result);


		}else {
			if(room.getPlayerList().size() == 3) {
				String result = MapHelper.newInstance()
						.put("roomId", room.getId())
						.put("roomOwner", room.getRoomOwner())
						.json();
				ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_FULL).call(player, result);

			}else {
				player.setRoomId(room.getId());

				ConcurrentSkipListMap<String, Player> roomClientMap = (ConcurrentSkipListMap<String, Player>) room.getPlayerMap();
				LinkedList<Player> roomClientList = room.getPlayerList();

				if(roomClientList.size() > 0){
					Player pre = roomClientList.getLast();
					pre.setNext(player);
					player.setPre(pre);
				}

				roomClientList.add(player);
				roomClientMap.put(player.getId(), player);

				if(roomClientMap.size() == 3) {
					player.setNext(roomClientList.getFirst());
					roomClientList.getFirst().setPre(player);
					SenderUtil.sendGroupMsg(room.getId(), "上桌成功. 现在已经有 " + room.getPlayerList().size() + " 位玩家在房间中. 游戏开始!");

					ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(player, String.valueOf(room.getId()));
				}else {
					room.setStatus(RoomStatus.WAIT);

					String result = MapHelper.newInstance()
							.put("clientId", player.getId())
							.put("clientNickname", player.getNickname())
							.put("roomId", room.getId())
							.put("roomOwner", room.getRoomOwner())
							.put("roomClientCount", room.getPlayerList().size())
							.json();
					SenderUtil.sendGroupMsg(room.getId(), "上桌成功. 现在已经有 " + room.getPlayerList().size() + " 位玩家在房间中.");
					for(Player client: roomClientMap.values()) {
						ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_SUCCESS).call(player, result);

					}

//					notifyWatcherJoinRoom(room, player);
				}
			}
		}
	}

	/**
	 * 通知观战者玩家加入房间
	 *
	 * @param room	房间
	 * @param player	玩家
	 */
	private void notifyWatcherJoinRoom(Room room, Player player) {
		for (Player watcher : room.getWatcherList()) {
			ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_SUCCESS).call(watcher, player.getNickname());

		}
	}
}
