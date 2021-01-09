package org.nico.ratel.landlords.server.event;

import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.enums.RoomStatus;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.ServerContains;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentSkipListMap;

public class ServerEventListener_CODE_ROOM_JOIN implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {

		Room room = ServerContains.getRoom(data);
		System.out.println(clientSide.getNickname()+"尝试加入房间");
		System.out.println(clientSide.getId()+"尝试加入房间");
		if(room == null) {
			String result = MapHelper.newInstance()
								.put("roomId", data)
								.json();
//			ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_INEXIST, result);
			ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_INEXIST).call(clientSide.getChannel(), result);


		}else {
			if(room.getClientSideList().size() == 3) {
				String result = MapHelper.newInstance()
						.put("roomId", room.getId())
						.put("roomOwner", room.getRoomOwner())
						.json();
//				ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_FULL, result);
				ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_FAIL_BY_FULL).call(clientSide.getChannel(), result);

			}else {
				clientSide.setRoomId(room.getId());

				ConcurrentSkipListMap<String, ClientSide> roomClientMap = (ConcurrentSkipListMap<String, ClientSide>) room.getClientSideMap();
				LinkedList<ClientSide> roomClientList = room.getClientSideList();

				if(roomClientList.size() > 0){
					ClientSide pre = roomClientList.getLast();
					pre.setNext(clientSide);
					clientSide.setPre(pre);
				}

				roomClientList.add(clientSide);
				roomClientMap.put(clientSide.getId(), clientSide);

				if(roomClientMap.size() == 3) {
					clientSide.setNext(roomClientList.getFirst());
					roomClientList.getFirst().setPre(clientSide);

					ServerEventListener.get(ServerEventCode.CODE_GAME_STARTING).call(clientSide, String.valueOf(room.getId()));
				}else {
					room.setStatus(RoomStatus.WAIT);

					String result = MapHelper.newInstance()
							.put("clientId", clientSide.getId())
							.put("clientNickname", clientSide.getNickname())
							.put("roomId", room.getId())
							.put("roomOwner", room.getRoomOwner())
							.put("roomClientCount", room.getClientSideList().size())
							.json();
					for(ClientSide client: roomClientMap.values()) {
//						ChannelUtils.pushToClient(client.getChannel(), ClientEventCode.CODE_ROOM_JOIN_SUCCESS, result);
						ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_SUCCESS).call(client.getChannel(), result);

					}

					notifyWatcherJoinRoom(room, clientSide);
				}
			}
		}
	}

	/**
	 * 通知观战者玩家加入房间
	 *
	 * @param room	房间
	 * @param clientSide	玩家
	 */
	private void notifyWatcherJoinRoom(Room room, ClientSide clientSide) {
		for (ClientSide watcher : room.getWatcherList()) {
//			ChannelUtils.pushToClient(watcher.getChannel(), ClientEventCode.CODE_ROOM_JOIN_SUCCESS, clientSide.getNickname());
			ClientEventListener.get(ClientEventCode.CODE_ROOM_JOIN_SUCCESS).call(watcher.getChannel(), clientSide.getNickname());

		}
	}
}
