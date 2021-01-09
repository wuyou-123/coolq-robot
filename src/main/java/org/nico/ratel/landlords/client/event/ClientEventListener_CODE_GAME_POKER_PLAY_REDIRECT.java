package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.landlordsPrint.FormatPrinter;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY_REDIRECT extends ClientEventListener {

    private static final String FORMAT = "[%s]  %s [%s] 剩余 %s";
    private static String[] choose = new String[]{"上家", "下家"};

    @SuppressWarnings("unchecked")
    @Override
    public void call(Channel channel, String data) {
        String qq = GetQQUtils.getQQ(channel);

        Map<String, Object> map = MapHelper.parser(data);

        String sellClientId = (String) map.get("sellClientId");

        List<Map<String, Object>> clientInfos = (List<Map<String, Object>>) map.get("clientInfos");

        for (int index = 0; index < 2; index++) {
            for (Map<String, Object> clientInfo : clientInfos) {
                String position = (String) clientInfo.get("position");
                if (position.equalsIgnoreCase(choose[index])) {
                    FormatPrinter.printNotice(qq, FORMAT, clientInfo.get("position"), clientInfo.get("clientNickname"), clientInfo.get("type"), clientInfo.get("surplus"));
                }
            }
        }
        if (sellClientId.equals(qq)) {
            GlobalVariable.THREAD_POOL.execute(() ->
                get(ClientEventCode.CODE_GAME_POKER_PLAY).call(channel, data)
            );
        } else {
            SimplePrinter.sendNotice(qq, "现在轮到" + map.get("sellClientNickname") + "出牌了, 请等待他/她出牌");
        }
    }

}
