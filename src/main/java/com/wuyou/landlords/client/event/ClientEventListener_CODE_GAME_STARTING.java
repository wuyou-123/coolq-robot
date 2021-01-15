package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.landlords.helper.PokerHelper;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.helper.MapHelper;

import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_STARTING extends ClientEventListener {

    @Override
    public void call(Player player, String data) {
        String qq = player.getId();

        Map<String, Object> map = MapHelper.parser(data);
        SenderUtil.sendPrivateMsg(qq, "游戏开始!");
        List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {
        });
        System.out.println(PokerHelper.printPoker(pokers));

///        SenderUtil.sendPrivateMsg(qq, "这是你的牌:");
///        SenderUtil.sendPrivateMsg(qq, "这是你的牌:\n" + PokerHelper.printPoker(pokers));
        SenderUtil.sendPrivateMsg(qq, "这是你的牌:\n" + CQ.getPoker(pokers));
///		SenderUtil.sendPrivateMsg(qq, CQ.getPoker(pokers);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        get(ClientEventCode.CODE_GAME_LANDLORD_ELECT).call(player, data);
    }

}
