package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_ELECT extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		String turnClientId = (String) map.get("nextClientId");
		
		if(map.containsKey("preClientNickname")) {
			SimplePrinter.sendNotice(qq, map.get("preClientNickname") + " 没有抢地主!");
		}
		
		if(turnClientId.equals(qq)) {
			SimplePrinter.sendNotice(qq, "现在轮到你了. 你是否要抢地主? [Y/N] (输入 [exit | e] 离开当前房间)");
			String line = SimpleWriter.write(qq, "Y/N");
//			String line = "Y";
			if("exit".equalsIgnoreCase(line) || "e".equalsIgnoreCase(line)) {
				ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(GetQQUtils.getClient(channel), null);
//				pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT);
			}else if("Y".equalsIgnoreCase(line)){
				ServerEventListener.get(ServerEventCode.CODE_GAME_LANDLORD_ELECT).call(GetQQUtils.getClient(channel), "TRUE");
//				pushToServer(channel, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "TRUE");
			}else if("N".equalsIgnoreCase(line)||"P".equalsIgnoreCase(line)){
				ServerEventListener.get(ServerEventCode.CODE_GAME_LANDLORD_ELECT).call(GetQQUtils.getClient(channel), "FALSE");
//				pushToServer(channel, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "FALSE");
			}else{
				SimplePrinter.sendNotice(qq, "无效的输入");
				call(channel, data);
			}
		}else {
			SimplePrinter.sendNotice(qq, "现在轮到" + map.get("nextClientNickname") + ". 请耐心等待他/她出牌!");
		}
		
	}

}
