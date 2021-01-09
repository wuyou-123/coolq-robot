package com.wuyou.robot.filter;

import com.wuyou.utils.landlordsPrint.SimplePrinter;
import lombok.SneakyThrows;
import love.forte.common.ioc.annotation.Beans;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.nico.ratel.landlords.enums.PokerLevel;
import org.nico.ratel.landlords.server.ServerContains;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wuyou
 */
@Beans("landlords")
public class LandlordsFilter implements ListenerFilter {

    @SneakyThrows
    @Override
    public boolean test(@NotNull FilterData data) {
        System.out.println("斗地主消息监听");
        if (data.getMsgGet() instanceof PrivateMsg) {
            PrivateMsg msg = (PrivateMsg) data.getMsgGet();
            String qq = msg.getAccountInfo().getAccountCode();
            LinkedBlockingQueue<String> queue = ServerContains.USER_INPUT.get(qq);
            if (queue != null) {
                System.out.println("插入一条消息");
                queue.put(msg.getMsg());
            }else{
                String[] strs = msg.getMsg().split(" ");
//                List<Character> options = new ArrayList<>();
                boolean access = true;
                for (String str : strs) {
                    for (char c : str.toCharArray()) {
                        if (c != ' ' && c != '\t') {
                            if (!PokerLevel.aliasContains(c)) {
                                access = false;
                                break;
                            }
///                            else {
///                                options.add(c);
///                            }
                        }
                    }
                }
                if(!access){
                    ServerContains.getRoomMap().get(ServerContains.CLIENT_SIDE_MAP.get(qq).getRoomId()).getClientSideList().forEach(clientSide ->
                            SimplePrinter.sendNotice(clientSide.getId(), qq + "说: " + msg.getMsg())
                    );
                }
            }

        }
        return false;
    }
}
