package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.ratel.landlords.helper.PokerHelper;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import org.nico.ratel.landlords.utils.GetQQUtils;
import org.nico.ratel.landlords.utils.OptionsUtils;

public class ClientEventListener_CODE_SHOW_OPTIONS_SETTING extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		SimplePrinter.sendNotice(qq, "Setting: ");
		SimplePrinter.sendNotice(qq, "1. Card with shape edges (Default)");
		SimplePrinter.sendNotice(qq, "2. Card with rounded edges");
		SimplePrinter.sendNotice(qq, "3. Text Only with types");
		SimplePrinter.sendNotice(qq, "4. Text Only without types");
		SimplePrinter.sendNotice(qq, "5. Unicode Cards");

		SimplePrinter.sendNotice(qq, "Please select an option above (enter [BACK] to return to options list)");
		String line = SimpleWriter.write(qq, "setting");

		if (!"BACK".equalsIgnoreCase(line)) {
			int choose = OptionsUtils.getOptions(line);

			if(choose >=1 && choose <= 5){
				PokerHelper.pokerPrinterType = choose - 1;
				//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);
			} else {
				SimplePrinter.sendNotice(qq, "Invalid setting, please choose again：");
				call(channel, data);
			}
		}  //get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, data);

	}



}
