package com.wuyou.robot.listeners;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.PokerLevel;
import com.wuyou.enums.RoomStatus;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.robot.BootClass;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import love.forte.common.ioc.annotation.Beans;
import love.forte.common.ioc.annotation.Depend;
import love.forte.simbot.annotation.Filter;
import love.forte.simbot.annotation.Filters;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.nico.noson.Noson;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.server.event.ServerEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author wuyou
 */
@Beans
public class LandlordsListener {

    @Depend
    private BootClass boot;

    @OnPrivate
    @Filters(customFilter = "privateLandlords")
    public void landlords(PrivateMsg msg, MsgSender sender) throws InterruptedException {
        System.out.println("斗地主监听");
        String qq = msg.getAccountInfo().getAccountCode();
        Player player = GlobalVariable.LANDLORDS_PLAYER.get(qq);
        String message = msg.getMsg();
        String[] strs = msg.getMsg().split(" ");
        boolean access = true;
        List<Character> options = new ArrayList<>();
        for (String str : strs) {
            for (char c : str.toCharArray()) {
                if (c != ' ' && c != '\t') {
                    if (PokerLevel.aliasContains(c)) {
                        access = false;
                        break;
                    } else {
                        options.add(c);
                    }
                }
            }
        }
        LinkedBlockingQueue<String> queue = GlobalVariable.USER_INPUT.get(qq);
        if (access) {
            ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(player, Noson.reversal(options.toArray(new Character[]{})));
            return;
        }
        if (queue != null && (player.getStatus() == ClientStatus.WAIT || player.getStatus() == ClientStatus.CALL_LANDLORD)) {
            System.out.println("111插入一条消息");
            queue.put(message);
        } else {
            Room room = GlobalVariable.getRoomById(qq);
            if (room != null) {
                if (room.getStatus() == RoomStatus.STOPPED) {
                    if ("继续".equals(message)) {
                        ServerEventListener.get(ServerEventCode.CODE_CLIENT_READY).call(player, null);
                    }
                }
                if ("exit".equalsIgnoreCase(message) || "e".equalsIgnoreCase(message)) {
                    ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(player, null);
                    return;
                }
                if ("help".equalsIgnoreCase(message) || "h".equalsIgnoreCase(message) || "斗地主帮助".equalsIgnoreCase(message)) {
//                    ServerEventListener.get(ServerEventCode.CODE_CLIENT_HELP).call(player, null);
                    return;
                }
                if (room.getPlayerList().size() == 1) {
                    SenderUtil.sendPrivateMsg(qq, "房间里只有你一个人了,你发送的消息只有你能看到哦");
                }
                room.getPlayerList().forEach(p ->
                        SenderUtil.sendPrivateMsg(p.getId(), player.getNickname() + "说: " + msg.getMsg())
                );
            }
        }
    }

    @OnGroup
    @Filters(customFilter = "groupLandlords")
    public void groupLandlords(GroupMsg msg, MsgSender sender) throws InterruptedException {
        String qq = msg.getAccountInfo().getAccountCode();
        Player player = GlobalVariable.LANDLORDS_PLAYER.get(qq);
        String message = msg.getMsg();
        String[] strs = msg.getMsg().split(" ");
        boolean access = true;
        List<Character> options = new ArrayList<>();
        for (String str : strs) {
            for (char c : str.toCharArray()) {
                if (c != ' ' && c != '\t') {
                    if (PokerLevel.aliasContains(c)) {
                        access = false;
                        break;
                    } else {
                        options.add(c);
                    }
                }
            }
        }
        LinkedBlockingQueue<String> queue = GlobalVariable.USER_INPUT.get(qq);
        if (access) {
            ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(player, Noson.reversal(options.toArray(new Character[]{})));
            return;
        }
        if (queue != null && (player.getStatus() == ClientStatus.WAIT || player.getStatus() == ClientStatus.CALL_LANDLORD)) {
            System.out.println("111插入一条消息");
            queue.put(message);
        } else {
            Room room = GlobalVariable.getRoomById(qq);
            if (room != null) {
                if (room.getStatus() == RoomStatus.STOPPED) {
                    if ("继续".equals(message)) {
                        ServerEventListener.get(ServerEventCode.CODE_CLIENT_READY).call(player, null);
                    }
                }
                if ("exit".equalsIgnoreCase(message) || "e".equalsIgnoreCase(message) || "下桌".equalsIgnoreCase(message)) {
                    ServerEventListener.get(ServerEventCode.CODE_CLIENT_EXIT).call(player, null);
                    return;
                }

                if ("help".equalsIgnoreCase(message) || "h".equalsIgnoreCase(message) || "斗地主帮助".equalsIgnoreCase(message)) {
//                    ServerEventListener.get(ServerEventCode.CODE_CLIENT_HELP).call(player, null);
                }
//                if (room.getPlayerList().size() == 1) {
//                    SenderUtil.sendPrivateMsg(qq, "房间里只有你一个人了,你发送的消息只有你能看到哦");
//                }
//                room.getPlayerList().forEach(p ->
//                        SenderUtil.sendPrivateMsg(p.getId(), player.getNickname() + "说: " + msg.getMsg())
//                );
            }
        }
    }

    @OnGroup
    @Filters(value = @Filter("重启斗地主"), customFilter = "boot")
    public void reloadLandlords(GroupMsg msg, MsgSender sender) {
        boot.initLandlords();
    }

    @OnGroup
    @Filters(value = @Filter("上桌"), customFilter = "boot")
    public void playLandlords(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
        String qq = msg.getAccountInfo().getAccountCode();
        String name = msg.getAccountInfo().getAccountRemarkOrNickname();
//        System.out.println(qq);
//        try {
//            FriendInfo info = sender.GETTER.getFriendInfo(qq);
//        } catch (NoSuchElementException e) {
//            System.out.println(e.getMessage());
//            SenderUtil.sendGroupMsg(group, "未添加好友,请先添加好友再加入游戏");
//            return;
//        }

        System.out.println(GlobalVariable.LANDLORDS_PLAYER);
        if (GlobalVariable.LANDLORDS_PLAYER.get(qq) == null) {
            Player player = new Player(qq, name);
            player.setNickname(String.valueOf(player.getId()));
            GlobalVariable.LANDLORDS_PLAYER.put(qq, player);
            System.out.println("playerId: " + player.getId());

            Room room = GlobalVariable.LANDLORDS_ROOM.get(group);
            try {
                if (room == null || GlobalVariable.getRoom(group) == null) {
                    ServerEventListener.get(ServerEventCode.CODE_ROOM_CREATE).call(player, group);
                } else {
                    ServerEventListener.get(ServerEventCode.CODE_ROOM_JOIN).call(player, room.getId() + "");
                }
            } catch (NoSuchElementException e) {
                System.out.println(e.getMessage());
                SenderUtil.sendGroupMsg(group, "未添加好友,请先添加好友再加入游戏");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            SenderUtil.sendGroupMsg(group, "你已经在桌上了");
        }
    }


    @OnGroup
    @Filters(value = @Filter("下桌"), customFilter = "boot")
    public void leaveLandlords(GroupMsg msg, MsgSender sender) {
        String group = msg.getGroupInfo().getGroupCode();
        String qq = msg.getAccountInfo().getAccountCode();
        String name = msg.getAccountInfo().getAccountRemarkOrNickname();
        Player player = GlobalVariable.LANDLORDS_PLAYER.get(qq);
        if (player != null) {
            Room room = GlobalVariable.getRoomById(qq);
            if (room != null && room.getStatus() != RoomStatus.STARTING) {
                GlobalVariable.LANDLORDS_PLAYER.remove(qq);
                GlobalVariable.USER_INPUT.remove(qq);
                room.getPlayerList().remove(room.getPlayerMap().remove(qq));
                if (room.getPlayerList().isEmpty()) {
                    GlobalVariable.removeRoom(group);
                    SenderUtil.sendGroupMsg(group, "下桌成功, 当前房间剩余" + room.getPlayerList().size() + "人, 已关闭房间");
                    return;
                }
                SenderUtil.sendGroupMsg(group, "下桌成功, 当前房间剩余" + room.getPlayerList().size() + "人");
///            } else {
///                SenderUtil.sendGroupMsg(group, "在游戏中");
            }
        } else {
//            SenderUtil.sendGroupMsg(group, "你不在桌上");
        }
    }
}
