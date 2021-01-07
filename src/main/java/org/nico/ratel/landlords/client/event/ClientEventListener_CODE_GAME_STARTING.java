package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.CQ;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_STARTING extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        String qq = GetQQUtils.getQQ(channel);

        Map<String, Object> map = MapHelper.parser(data);
        SimplePrinter.sendNotice(qq, "游戏开始!");
        System.out.println(map.get("pokers"));
        List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});

        SimplePrinter.sendNotice(qq, "这是你的牌:");
//        SimplePrinter.sendNotice(qq, "这是你的牌:\n" + PokerHelper.printPoker(pokers));
		SimplePrinter.printPokers(qq, pokers);

        get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(channel, data);
    }

}
