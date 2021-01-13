package org.nico.ratel.landlords.server.event;

import com.wuyou.utils.GlobalVariable;
import org.nico.noson.Noson;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import com.wuyou.entity.Player;
import org.nico.ratel.landlords.entity.Room;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.enums.RoomStatus;
import com.wuyou.enums.RoomType;

public class ServerEventListener_CODE_ROOM_CREATE implements ServerEventListener{

	@Override
	public void call(Player player, String data) {
		
		Room room = new Room(data);
		room.setStatus(RoomStatus.BLANK);
		room.setType(RoomType.PVP);
		room.setRoomOwner(player.getNickname());
		room.getPlayerMap().put(player.getId(), player);
		room.getPlayerList().add(player);
		room.setCurrentSellClient(player.getId());
		room.setCreateTime(System.currentTimeMillis());
		room.setLastFlushTime(System.currentTimeMillis());
		
		player.setRoomId(room.getId());
		GlobalVariable.addRoom(room);
		GlobalVariable.LANDLORDS_ROOM.put(data, room);
		Room room1 = GlobalVariable.LANDLORDS_ROOM.get(data);
		System.out.println("房间号: " + room1.getId());
//		ChannelUtils.pushToClient(player, ClientEventCode.CODE_ROOM_CREATE_SUCCESS, Noson.reversal(room));
		ClientEventListener.get(ClientEventCode.CODE_ROOM_CREATE_SUCCESS).call(player, Noson.reversal(room));

	}

	



}
