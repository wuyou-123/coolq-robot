package com.wuyou.utils.landlordsPrint;

import com.wuyou.utils.SenderUtil;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.server.ServerContains;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleWriter {

//    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static String write(String qq, String message) {
        try {
            SenderUtil.sendPrivateMsg(qq, "请输入" + message);
            ServerContains.USER_INPUT.put(qq, new LinkedBlockingQueue<>(1));
            String input = ServerContains.USER_INPUT.get(qq).poll(20, TimeUnit.SECONDS);
            if (input == null) {
                input = "p";
            }
            ServerContains.USER_INPUT.remove(qq);
            return input;
        } catch (InterruptedException e) {
///            System.out.println("用户超时,调用跳过方法");
///            ChannelUtils.pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_PASS, null);
            System.out.println("用户超时,返回pass");
            return "p";
        }
    }

    public static String write(Channel channel, String message) {
        String input = write(GetQQUtils.getQQ(channel), message);
        System.out.println(input);
        return input;
    }

}
