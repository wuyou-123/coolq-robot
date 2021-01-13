package com.wuyou.utils;

import org.nico.ratel.landlords.entity.Room;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

/**
 * @author admin
 * @date 2020年08月01日 14:29
 */
@Component
public class SenderUtil {
//    private static BotManager botManager;
//
//    @Autowired
//    public void setBotManager(BotManager manager) {
//        setManager(manager);
//    }
//
//    public static void setManager(final BotManager manager) {
//        botManager = manager;
//    }

    public static void sendGroupMsg(String fromGroup, String msg) {
        if (CQ.CONTEXT.get(fromGroup) == null) {
            CQ.CONTEXT.put(fromGroup, new String[]{"", "", "", "", msg});
            GlobalVariable.botManager.getDefaultBot().getSender().SENDER.sendGroupMsg(fromGroup, msg);
            return;
        }
        String[] ret = getList(fromGroup);
        CQ.CONTEXT.put(fromGroup, new String[]{ret[1], ret[2], ret[3], ret[4], msg});
        GlobalVariable.botManager.getDefaultBot().getSender().SENDER.sendGroupMsg(fromGroup, msg);
    }

    public static void sendPrivateMsg(String qq, String msg) {
        sendPrivateMsg(qq, null, msg);
    }

    public static void sendPrivateMsg(String qq, String group, String msg) {
        try {
            Room room = GlobalVariable.getRoomById(qq);
            if (room != null) {
                GlobalVariable.botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(qq, room.getId(), msg);
            } else {
                GlobalVariable.botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(qq, group, msg);
            }
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            SenderUtil.sendPrivateMsg(GlobalVariable.ADMINISTRATOR.get(0), "尝试给[" + qq + "]发送消息: " + msg + " 失败");
        }
    }

    public static String[] getList(String fromGroup) {
        return CQ.CONTEXT.get(fromGroup);
    }


}
