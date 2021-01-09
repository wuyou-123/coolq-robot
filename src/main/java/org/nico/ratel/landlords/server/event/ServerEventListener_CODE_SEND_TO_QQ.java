package org.nico.ratel.landlords.server.event;

import org.nico.ratel.landlords.entity.ClientSide;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

public class ServerEventListener_CODE_SEND_TO_QQ implements ServerEventListener{

	@Override
	public void call(ClientSide clientSide, String data) {
		String qq = GetQQUtils.getQQ(clientSide.getChannel());
		System.out.println("给"+qq+"发送内容: "+data);
		SimplePrinter.sendNotice(qq,"从SEND_TO_QQ发送===="+ data);
	}

}
