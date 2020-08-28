package com.wuyou.utils;

import com.forte.qqrobot.sender.MsgSender;

import java.util.Arrays;

/**
 * @Description: TODO
 * @author: admin
 * @date: 2020年08月01日 14:29
 */
public class SenderUtil {

    public static void sendGroupMsg(MsgSender sender, String fromGroup, String msg) {
        if (CQ.context.get(fromGroup) == null) {
            System.out.println(Arrays.toString(CQ.context.get(fromGroup)));
            CQ.context.put(fromGroup, new String[]{"", "", "", "", msg});
            sender.SENDER.sendGroupMsg(fromGroup, msg);
            return;
        }
        String[] ret = getList(fromGroup);
        CQ.context.put(fromGroup, new String[]{ret[1], ret[2], ret[3], ret[4], msg});
        System.out.println(Arrays.toString(CQ.context.get(fromGroup)));
        sender.SENDER.sendGroupMsg(fromGroup, msg);
    }

    public static String[] getList(String fromGroup){
        return CQ.context.get(fromGroup);
    }

}
