package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.print.FormatPrinter;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_SHOW_ROOMS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		List<Map<String, Object>> roomList = Noson.convert(data, new NoType<List<Map<String, Object>>>() {});
		if(roomList != null && ! roomList.isEmpty()){
			// "COUNT" begins after NICKNAME_MAX_LENGTH characters. The dash means that the string is left-justified.
			String format = "#\t%s\t|\t%-" + 15 + "s\t|\t%-6s\t|\t%-6s\t#\n";
			FormatPrinter.printNotice(format, "ID", "OWNER", "COUNT", "TYPE");
			for(Map<String, Object> room: roomList) {
				SimplePrinter.sendNotice(qq, room.get("roomId").toString());
				FormatPrinter.printNotice(format, room.get("roomId"), room.get("roomOwner"), room.get("roomClientCount"), room.get("roomType"));
			}
			SimplePrinter.sendNotice(qq, "");
			//get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
		}else {
			SimplePrinter.sendNotice(qq, "没有可用的房间,请创建房间!");
			//get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
		}
	}



}
