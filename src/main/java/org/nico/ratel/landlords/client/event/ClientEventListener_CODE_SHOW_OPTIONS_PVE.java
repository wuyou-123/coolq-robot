package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import org.nico.ratel.landlords.utils.GetQQUtils;
import org.nico.ratel.landlords.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_PVE extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);

		SimplePrinter.sendNotice(qq, "PVE: ");
		SimplePrinter.sendNotice(qq, "1. Easy Mode");
		SimplePrinter.sendNotice(qq, "2. Medium Mode");
		SimplePrinter.sendNotice(qq, "3. Hard Mode");
		SimplePrinter.sendNotice(qq, "Please select an option above (enter [back|b] to return to options list)");
		String line = SimpleWriter.write(qq, "pve");

		if (!"back".equalsIgnoreCase(line) && !"b".equalsIgnoreCase(line)) {
			int choose = OptionsUtils.getOptions(line);

			if(0 < choose && choose < 4) {
				initLastSellInfo();
				pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE_PVE, String.valueOf(choose));
			}else {
				SimplePrinter.sendNotice(qq, "Invalid option, please choose again：");
				call(channel, data);
			}
		}
		//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);


	}



}
