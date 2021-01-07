package org.nico.ratel.landlords.server.event;

import com.wuyou.utils.GlobalVariable;
import org.nico.noson.Noson;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.enums.RoomStatus;
import org.nico.ratel.landlords.enums.RoomType;
import org.nico.ratel.landlords.server.ServerContains;

public class ServerEventListener_CODE_ROOM_CREATE implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		
		Room room = new Room(ServerContains.getServerId());
		room.setStatus(RoomStatus.BLANK);
		room.setType(RoomType.PVP);
		room.setRoomOwner(clientSide.getNickname());
		room.getClientSideMap().put(clientSide.getId(), clientSide);
		room.getClientSideList().add(clientSide);
		room.setCurrentSellClient(clientSide.getId());
		room.setCreateTime(System.currentTimeMillis());
		room.setLastFlushTime(System.currentTimeMillis());
		
		clientSide.setRoomId(room.getId());
		ServerContains.addRoom(room);
		System.out.println(ServerContains.CLIENT_SIDE_MAP);
		GlobalVariable.LANDLORDS_ROOM.put(data, room);
		Room room1 = GlobalVariable.LANDLORDS_ROOM.get(data);
		System.out.println("房间号: " + room1.getId());
//		ChannelUtils.pushToClient(clientSide.getChannel(), ClientEventCode.CODE_ROOM_CREATE_SUCCESS, Noson.reversal(room));
		ClientEventListener.get(ClientEventCode.CODE_ROOM_CREATE_SUCCESS).call(clientSide.getChannel(), Noson.reversal(room));

	}

	



}
