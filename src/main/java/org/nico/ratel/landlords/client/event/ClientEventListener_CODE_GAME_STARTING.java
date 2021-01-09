package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.CQ;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.helper.PokerHelper;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_STARTING extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        String qq = GetQQUtils.getQQ(channel);

        Map<String, Object> map = MapHelper.parser(data);
        SimplePrinter.sendNotice(qq, "游戏开始!");
        List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {
        });
        System.out.println(PokerHelper.printPoker(pokers));

///        SimplePrinter.sendNotice(qq, "这是你的牌:");
///        SimplePrinter.sendNotice(qq, "这是你的牌:\n" + PokerHelper.printPoker(pokers));
        SimplePrinter.sendNotice(qq, "这是你的牌:\n" + CQ.getPoker(pokers));
///		SimplePrinter.printPokers(qq, pokers);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(channel, data);
    }

}
