package com.wuyou.utils.landlordsPrint;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientStatus;
import com.wuyou.utils.GlobalVariable;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleWriter {

//    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static String write(String qq, String message) {
        try {
//            SenderUtil.sendPrivateMsg(qq, "请输入" + message);

            GlobalVariable.USER_INPUT.put(qq, new LinkedBlockingQueue<>(1));
            String input = GlobalVariable.USER_INPUT.get(qq).poll(40, TimeUnit.SECONDS);
            if (input == null) {
                input = "p";
            }
            GlobalVariable.USER_INPUT.remove(qq);
            try {
                GlobalVariable.LANDLORDS_PLAYER.get(qq).setStatus(ClientStatus.PLAYING);
            }catch (Exception ignored){}
            return input;
        } catch (InterruptedException e) {
///            System.out.println("用户超时,调用跳过方法");
///            ChannelUtils.pushToServer(player, ServerEventCode.CODE_GAME_POKER_PLAY_PASS, null);
            System.out.println("用户超时,返回pass");
            return "p";
        }
    }

    public static String write(Player player, String message) {
        String input = write(player.getId(), message);
        System.out.println(input);
        return input;
    }

}
