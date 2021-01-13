package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import org.nico.ratel.landlords.helper.MapHelper;

import java.util.Formatter;
import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_REDIRECT extends ClientEventListener {

    private static final String FORMAT = "[%s]  %s [%s] 剩余 %s";
    private static String[] choose = new String[]{"上家", "下家"};

    @SuppressWarnings("unchecked")
    @Override
    public void call(Player player, String data) {
        String qq = player.getId();

        Map<String, Object> map = MapHelper.parser(data);

        String sellClientId = (String) map.get("sellClientId");

        List<Map<String, Object>> clientInfos = (List<Map<String, Object>>) map.get("clientInfos");
        StringBuilder sb = new StringBuilder();
        for (int index = 0; index < 2; index++) {
            for (Map<String, Object> clientInfo : clientInfos) {
                String position = (String) clientInfo.get("position");
                if (position.equalsIgnoreCase(choose[index])) {
                    sb.append(new Formatter().format(FORMAT, clientInfo.get("position"), clientInfo.get("clientNickname"), clientInfo.get("type"), clientInfo.get("surplus")).toString());
                    sb.append("\n");
                }
            }
        }
        SenderUtil.sendPrivateMsg(qq, sb.substring(0, sb.length() - 1));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (sellClientId.equals(qq)) {
            GlobalVariable.THREAD_POOL.execute(() ->
                    get(ClientEventCode.CODE_GAME_POKER_PLAY).call(player, data)
            );
        } else {
            SenderUtil.sendPrivateMsg(qq, "现在轮到" + map.get("sellClientNickname") + "出牌了, 请等待他/她出牌");
        }
    }

}
