package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import org.nico.ratel.landlords.utils.GetQQUtils;
import org.nico.ratel.landlords.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVP extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		SimplePrinter.sendNotice(qq, "PVP: ");
		SimplePrinter.sendNotice(qq, "1. Create Room");
		SimplePrinter.sendNotice(qq, "2. Room List");
		SimplePrinter.sendNotice(qq, "3. Join Room");
		SimplePrinter.sendNotice(qq, "4. Spectate Game");
		SimplePrinter.sendNotice(qq, "Please select an option above (enter [back|b] to return to options list)");
		String line = SimpleWriter.write(qq, "pvp");
		if (!"back".equalsIgnoreCase(line) && !"b".equalsIgnoreCase(line)) {
			int choose = OptionsUtils.getOptions(line);

			if(choose == 1) {
				pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE, null);
			}else if(choose == 2){
				pushToServer(channel, ServerEventCode.CODE_GET_ROOMS, null);
			}else if(choose == 3){
				SimplePrinter.sendNotice(qq, "Please enter the room id you wish to join (enter [back|b] to return to options list)");
				line = SimpleWriter.write(qq, "roomid");

				if("back".equalsIgnoreCase(line) || "b".equalsIgnoreCase(line)) {
					call(channel, data);
				}else {
					int option = OptionsUtils.getOptions(line);
					if(option < 1) {
						SimplePrinter.sendNotice(qq, "Invalid option, please choose again：");
						call(channel, data);
					}else{
						pushToServer(channel, ServerEventCode.CODE_ROOM_JOIN, String.valueOf(option));
					}
				}
			} else if (choose == 4) {
				SimplePrinter.sendNotice(qq, "Please enter the room id you want to spectate (enter [back] to return to options list)");
				line = SimpleWriter.write(qq, "roomid");

				if("back".equalsIgnoreCase(line) || "b".equalsIgnoreCase(line)) {
					call(channel, data);
				}else {
					int option = OptionsUtils.getOptions(line);
					if(option < 1) {
						SimplePrinter.sendNotice(qq, "Invalid option, please choose again：");
						call(channel, data);
					}else{
						pushToServer(channel, ServerEventCode.CODE_GAME_WATCH, String.valueOf(option));
					}
				}
			} else {
				SimplePrinter.sendNotice(qq, "Invalid option, please choose again：");
				call(channel, data);
			}
		}
//		else {
			///get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
//		}

	}



}
