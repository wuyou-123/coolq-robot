package com.wuyou.landlords.client.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.CQ;
import com.wuyou.utils.SenderUtil;
import com.wuyou.utils.landlordsPrint.SimpleWriter;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.entity.PokerSell;
import com.wuyou.landlords.helper.MapHelper;
import com.wuyou.landlords.helper.PokerHelper;
import com.wuyou.landlords.server.event.ServerEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wuyou
 */
public class ClientEventListener_CODE_GAME_POKER_PLAY extends ClientEventListener {

    @Override
    public void call(Player player, String data) {
        String qq = player.getId();
        Map<String, Object> map = MapHelper.parser(data);
        if (player.get("count")!=null && Integer.parseInt(player.get("count")) > 3) {
            ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(player, null);
            return;
        }
        SenderUtil.sendPrivateMsg(qq, "轮到你出牌了, 你的牌如下: ");
        List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {
        });
        SenderUtil.sendPrivateMsg(qq, CQ.getPoker(pokers));

        SenderUtil.sendPrivateMsg(qq, "请输入您想出的牌 (输入 [exit|e] 离开当前房间, 输入 [pass|p] 跳过这一回合, 输入 [view|v] 查看自己的牌, 输入 [help|h] 查看帮助信息)");
        player.setStatus(ClientStatus.WAIT);
        String line = SimpleWriter.write(qq, "你要出的牌");
        if ("pass".equalsIgnoreCase(line) || "p".equalsIgnoreCase(line)) {
            ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_PASS).call(player, null);
        } else if ("exit".equalsIgnoreCase(line) || "e".equalsIgnoreCase(line)) {
        } else if ("help".equalsIgnoreCase(line) || "h".equalsIgnoreCase(line)) {
        } else if ("view".equalsIgnoreCase(line) || "v".equalsIgnoreCase(line)) {
            if (!map.containsKey("lastSellPokers") || !map.containsKey("lastSellClientId")) {
                SenderUtil.sendPrivateMsg(qq, "当前服务器版本不支持此特性，需要超过v1.2.4");
                call(player, data);
                return;
            }
            Object lastSellPokersObj = map.get("lastSellPokers");
            if (lastSellPokersObj == null || qq.equals(map.get("lastSellClientId"))) {
                call(player, data);
            } else {
                List<Poker> lastSellPokers = Noson.convert(lastSellPokersObj, new NoType<List<Poker>>() {
                });
                List<PokerSell> sells = PokerHelper.validSells(PokerHelper.checkPokerType(lastSellPokers), pokers);
                if (sells.size() == 0) {
                    SenderUtil.sendPrivateMsg(qq, "没有牌能大过大家...");
                    call(player, data);
                    return;
                }
                for (int i = 0; i < sells.size(); i++) {
                    SenderUtil.sendPrivateMsg(qq, i + 1 + ". " + PokerHelper.textOnlyNoType(sells.get(i).getSellPokers()));
                }
                while (true) {
                    SenderUtil.sendPrivateMsg(qq, "你可以选择一个编号.(输入 [back | b] 返回.)");
                    player.setStatus(ClientStatus.WAIT);
                    line = SimpleWriter.write(qq, "编号");
                    if ("back".equalsIgnoreCase(line) || "b".equalsIgnoreCase(line)) {
                        call(player, data);
                        return;
                    } else {
                        try {
                            int choose = Integer.parseInt(line);
                            if (choose < 1 || choose > sells.size()) {
                                SenderUtil.sendPrivateMsg(qq, "输入的编号必须是从1到" + sells.size() + ".");
                            } else {
                                List<Poker> choosePokers = sells.get(choose - 1).getSellPokers();
                                List<Character> options = new ArrayList<>();
                                for (Poker poker : choosePokers) {
                                    options.add(poker.getLevel().getAlias()[0]);
                                }
                                ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(player, Noson.reversal(options.toArray(new Character[]{})));
                                break;
                            }
                        } catch (NumberFormatException e) {
                            SenderUtil.sendPrivateMsg(qq, "请输入一个数字.");
                        }
                    }
                }
            }
///				PokerHelper.validSells(lastPokerSell, pokers);
//        } else {
//            List<Character> options = new ArrayList<>();
//            boolean access = true;
//            for (String str : line.split(" ")) {
//                for (char c : str.toCharArray()) {
//                    if (c != ' ' && c != '\t') {
//                        if (!PokerLevel.aliasContains(c)) {
//                            access = false;
//                            break;
//                        } else {
//                            options.add(c);
//                        }
//                    }
//                }
//            }
//            if (access) {
//                ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(player, Noson.reversal(options.toArray(new Character[]{})));
/////					pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY, Noson.reversal(options.toArray(new Character[] {})));
//            } else {
//                String finalLine = line;
//                GlobalVariable.getRoomMap().get(player.getRoomId()).getPlayerList().forEach(player1 ->
//                        SenderUtil.sendPrivateMsg(player1.getId(), player.getNickname() + "说: " + finalLine)
//                );
//
/////                    if (lastPokers != null) {
/////                        SenderUtil.sendPrivateMsg(qq, lastSellClientNickname + "[" + lastSellClientType + "] played:");
/////                        SenderUtil.sendPrivateMsg(qq, CQ.getPoker(lastPokers);
/////                    }
/////
//                call(player, data);
//            }
        }
    }
}
