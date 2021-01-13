package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.SenderUtil;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.server.event.ServerEventListener;

import java.util.Map;

public class ClientEventListener_CODE_GAME_LANDLORD_ELECT extends ClientEventListener {

    @Override
    public void call(Player player, String data) {
        String qq = player.getId();
        Map<String, Object> map = MapHelper.parser(data);
        String turnClientId = (String) map.get("nextClientId");

        if (map.containsKey("preClientNickname")) {
            SenderUtil.sendPrivateMsg(qq, map.get("preClientNickname") + " 没有抢地主!");
        }

        if (turnClientId.equals(qq)) {
            SenderUtil.sendPrivateMsg(qq, "现在轮到你了. 你是否要抢地主? [Y/N] (输入 [exit | e] 离开当前房间)");
            player.setStatus(ClientStatus.CALL_LANDLORD);
            String line = SimpleWriter.write(qq, "Y/N");
//			String line = "Y";
            if ("exit".equalsIgnoreCase(line) || "e".equalsIgnoreCase(line)) {
                ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(player, null);
//				pushToServer(player, ServerEventCode.CODE_CLIENT_EXIT);
            } else if ("Y".equalsIgnoreCase(line)) {
                ServerEventListener.get(ServerEventCode.CODE_GAME_LANDLORD_ELECT).call(player, "TRUE");
//				pushToServer(player, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "TRUE");
            } else if ("N".equalsIgnoreCase(line) || "P".equalsIgnoreCase(line)) {
                ServerEventListener.get(ServerEventCode.CODE_GAME_LANDLORD_ELECT).call(player, "FALSE");
//				pushToServer(player, ServerEventCode.CODE_GAME_LANDLORD_ELECT, "FALSE");
            } else {
                SenderUtil.sendPrivateMsg(qq, "无效的输入");
                call(player, data);
            }
        } else {
            SenderUtil.sendPrivateMsg(qq, "现在轮到" + map.get("nextClientNickname") + ". 请耐心等待他/她选择是否抢地主!");
        }

    }

}
