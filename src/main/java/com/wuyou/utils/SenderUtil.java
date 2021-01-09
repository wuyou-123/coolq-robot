package com.wuyou.utils;

import love.forte.simbot.api.sender.MsgSender;
import love.forte.simbot.bot.BotManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author admin
 * @date 2020年08月01日 14:29
 */
@Component
public class SenderUtil {
    private static BotManager botManager;

    @Autowired
    public void setBotManager(BotManager manager) {
        setManager(manager);
    }
    public static void setManager(final BotManager manager) {
        botManager = manager;
    }
    public static void sendGroupMsg(MsgSender sender, String fromGroup, String msg) {
        if (CQ.CONTEXT.get(fromGroup) == null) {
            CQ.CONTEXT.put(fromGroup, new String[]{"", "", "", "", msg});
            sender.SENDER.sendGroupMsg(fromGroup, msg);
            return;
        }
        String[] ret = getList(fromGroup);
        CQ.CONTEXT.put(fromGroup, new String[]{ret[1], ret[2], ret[3], ret[4], msg});
        sender.SENDER.sendGroupMsg(fromGroup, msg);
    }

    public static void sendPrivateMsg(String qq, String msg) {
        botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(qq, msg);
    }

    public static String[] getList(String fromGroup) {
        return CQ.CONTEXT.get(fromGroup);
    }

}
