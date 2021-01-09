package org.nico.ratel.landlords.client.event;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.entity.PokerSell;
import org.nico.ratel.landlords.enums.PokerLevel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.helper.PokerHelper;
import org.nico.ratel.landlords.server.ServerContains;
import org.nico.ratel.landlords.server.event.ServerEventListener;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wuyou
 */
public class ClientEventListener_CODE_GAME_POKER_PLAY extends ClientEventListener {

    @Override
    public void call(Channel channel, String data) {
        String qq = GetQQUtils.getQQ(channel);
        Map<String, Object> map = MapHelper.parser(data);
        if (channel.hasAttr(GlobalVariable.ATTRIBUTE_KEY_NUMBER) && channel.attr(GlobalVariable.ATTRIBUTE_KEY_NUMBER).get() > 3) {
            ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(GetQQUtils.getClient(channel), null);
            return;
        }
        SimplePrinter.sendNotice(qq, "轮到你出牌了, 你的牌如下: ");
        List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {
        });
        SimplePrinter.printPokers(qq, pokers);

        SimplePrinter.sendNotice(qq, "请输入您想出的牌 (输入 [exit|e] 离开当前房间, 输入 [pass|p] 跳过这一回合, 输入 [view|v] 查看自己的牌)");
        String line = SimpleWriter.write(qq, "你要出的牌");
        if ("pass".equalsIgnoreCase(line) || "p".equalsIgnoreCase(line)) {
            ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_PASS).call(GetQQUtils.getClient(channel), null);
//				pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_PASS);
        } else if ("exit".equalsIgnoreCase(line) || "e".equalsIgnoreCase(line)) {
            ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(GetQQUtils.getClient(channel), null);
//				pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT);
        } else if ("view".equalsIgnoreCase(line) || "v".equalsIgnoreCase(line)) {
            if (!map.containsKey("lastSellPokers") || !map.containsKey("lastSellClientId")) {
                SimplePrinter.sendNotice(qq, "当前服务器版本不支持此特性，需要超过v1.2.4");
                call(channel, data);
                return;
            }
            Object lastSellPokersObj = map.get("lastSellPokers");
            if (lastSellPokersObj == null || qq.equals(map.get("lastSellClientId"))) {
                call(channel, data);
            } else {
                List<Poker> lastSellPokers = Noson.convert(lastSellPokersObj, new NoType<List<Poker>>() {
                });
                List<PokerSell> sells = PokerHelper.validSells(PokerHelper.checkPokerType(lastSellPokers), pokers);
                if (sells.size() == 0) {
                    SimplePrinter.sendNotice(qq, "没有牌能大过大家...");
                    call(channel, data);
                    return;
                }
                for (int i = 0; i < sells.size(); i++) {
                    SimplePrinter.sendNotice(qq, i + 1 + ". " + PokerHelper.textOnlyNoType(sells.get(i).getSellPokers()));
                }
                while (true) {
                    SimplePrinter.sendNotice(qq, "你可以选择一个编号.(输入 [back | b] 返回.)");
                    line = SimpleWriter.write(qq, "编号");
                    if ("back".equalsIgnoreCase(line) || "b".equalsIgnoreCase(line)) {
                        call(channel, data);
                        return;
                    } else {
                        try {
                            int choose = Integer.parseInt(line);
                            if (choose < 1 || choose > sells.size()) {
                                SimplePrinter.sendNotice(qq, "输入的编号必须是从1到" + sells.size() + ".");
                            } else {
                                List<Poker> choosePokers = sells.get(choose - 1).getSellPokers();
                                List<Character> options = new ArrayList<>();
                                for (Poker poker : choosePokers) {
                                    options.add(poker.getLevel().getAlias()[0]);
                                }
                                ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(GetQQUtils.getClient(channel), Noson.reversal(options.toArray(new Character[]{})));
///									pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY, Noson.reversal(options.toArray(new Character[] {})));
                                break;
                            }
                        } catch (NumberFormatException e) {
                            SimplePrinter.sendNotice(qq, "请输入一个数字.");
                        }
                    }
                }
            }
///				PokerHelper.validSells(lastPokerSell, pokers);
        } else {
            List<Character> options = new ArrayList<>();
            boolean access = true;
            for (String str : line.split(" ")) {
                for (char c : str.toCharArray()) {
                    if (c != ' ' && c != '\t') {
                        if (!PokerLevel.aliasContains(c)) {
                            access = false;
                            break;
                        } else {
                            options.add(c);
                        }
                    }
                }
            }
            if (access) {
                ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(GetQQUtils.getClient(channel), Noson.reversal(options.toArray(new Character[]{})));
///					pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY, Noson.reversal(options.toArray(new Character[] {})));
            } else {
                String finalLine = line;
                ServerContains.getRoomMap().get(GetQQUtils.getClient(channel).getRoomId()).getClientSideList().forEach(clientSide ->
                        SimplePrinter.sendNotice(clientSide.getId(), qq + "说: " + finalLine)
                );

///                    if (lastPokers != null) {
///                        SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] played:");
///                        SimplePrinter.printPokers(qq, lastPokers);
///                    }
///
                call(channel, data);
            }
        }
    }
}
