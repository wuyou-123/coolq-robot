package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.print.SimpleWriter;
import org.nico.ratel.landlords.utils.GetQQUtils;
import org.nico.ratel.landlords.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		SimplePrinter.sendNotice(data,"欢迎来到斗地主");

		String qq = GetQQUtils.getQQ(channel);
		pushToServer(channel, ServerEventCode.CODE_SEND_TO_QQ, "欢迎来到斗地主");
//		//get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
		SimplePrinter.printNotice(qq,"Options: ");
		SimplePrinter.printNotice(qq,"1. PvP");
		SimplePrinter.printNotice(qq,"2. PvE");
		SimplePrinter.printNotice(qq,"3. Settings");
		SimplePrinter.printNotice(qq,"Please select an option above (enter [exit|e] to log out)");
		String line = SimpleWriter.write("selection");

		if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")) {
			System.exit(0);
		}else {
			int choose = OptionsUtils.getOptions(line);

			if(choose == 1) {
				//get(ClientEventCode.CODE_SHOW_OPTIONS_PVP).call(channel, data);
			}else if(choose == 2){
				//get(ClientEventCode.CODE_SHOW_OPTIONS_PVE).call(channel, data);
			}else if(choose == 3){
				//get(ClientEventCode.CODE_SHOW_OPTIONS_SETTING).call(channel, data);
			}else {
				SimplePrinter.printNotice(qq, "Invalid option, please choose again：");
				call(channel, data);
			}
		}
	}



}
