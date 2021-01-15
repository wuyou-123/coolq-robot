package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;
import com.wuyou.enums.ClientRole;
import com.wuyou.enums.SellType;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.landlords.entity.Room;
import com.wuyou.landlords.helper.PokerHelper;
import com.wuyou.utils.CQ;
import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.SenderUtil;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import com.wuyou.landlords.client.event.ClientEventListener;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.entity.PokerSell;
import com.wuyou.landlords.helper.MapHelper;
import com.wuyou.landlords.server.robot.RobotEventListener;

import java.util.List;
import java.util.Objects;

public class ServerEventListener_CODE_GAME_POKER_PLAY implements ServerEventListener {

    @Override
    public void call(Player player, String data) {
        Room room = GlobalVariable.getRoom(player.getRoomId());
        if (room != null) {
            if (room.getCurrentSellClient().equals(player.getId())) {
                Character[] options = Noson.convert(data, Character[].class);
                int[] indexes = PokerHelper.getIndexes(options, player.getPokers());
                if (PokerHelper.checkPokerIndex(indexes, player.getPokers())) {
                    boolean sellFlag = true;

                    assert indexes != null;
                    List<Poker> currentPokers = PokerHelper.getPoker(indexes, player.getPokers());
                    PokerSell currentPokerShell = PokerHelper.checkPokerType(currentPokers);
                    if (currentPokerShell.getSellType() != SellType.ILLEGAL) {
                        if (!room.getLastSellClient().equals(player.getId()) && room.getLastPokerShell() != null) {
                            PokerSell lastPokerShell = room.getLastPokerShell();

                            if ((lastPokerShell.getSellType() != currentPokerShell.getSellType() || lastPokerShell.getSellPokers().size() != currentPokerShell.getSellPokers().size()) && currentPokerShell.getSellType() != SellType.BOMB && currentPokerShell.getSellType() != SellType.KING_BOMB) {
                                String result = MapHelper.newInstance()
                                        .put("playType", currentPokerShell.getSellType())
                                        .put("playCount", currentPokerShell.getSellPokers().size())
                                        .put("preType", lastPokerShell.getSellType())
                                        .put("preCount", lastPokerShell.getSellPokers().size())
                                        .json();
                                sellFlag = false;
///								ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_MISMATCH, result);
                                ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_MISMATCH).call(player, result);

                            } else if (lastPokerShell.getScore() >= currentPokerShell.getScore()) {
                                String result = MapHelper.newInstance()
                                        .put("playScore", currentPokerShell.getScore())
                                        .put("preScore", lastPokerShell.getScore())
                                        .json();
                                sellFlag = false;
///								ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_LESS, result);
                                ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_LESS).call(player, result);

                            }
                        }
                    } else {
                        sellFlag = false;
///						ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_INVALID, null);
                        ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_INVALID).call(player, null);

                    }

                    if (sellFlag) {
                        Player next = player.getNext();

                        room.setLastSellClient(player.getId());
                        room.setLastPokerShell(currentPokerShell);
                        room.setCurrentSellClient(next.getId());

                        player.getPokers().removeAll(currentPokers);
                        MapHelper mapHelper = MapHelper.newInstance()
                                .put("clientId", player.getId())
                                .put("clientNickname", player.getNickname())
                                .put("clientType", player.getType())
                                .put("pokers", currentPokers)
                                .put("roomId", room.getId())
                                .put("lastSellClientId", player.getId())
                                .put("lastSellPokers", currentPokers);

                        SenderUtil.sendGroupMsg(room.getId(), player.getNickname() + "[" + player.getType() + "] 出牌:");
                        List<Poker> lastPokers = Noson.convert(currentPokers, new NoType<List<Poker>>() {
                        });
                        SenderUtil.sendGroupMsg(room.getId(), CQ.getPoker(lastPokers));
                        if (!player.getPokers().isEmpty()) {
                            mapHelper.put("sellClientNickname", next.getNickname());
                            SenderUtil.sendGroupMsg(room.getId(), "下一位玩家是 " + next.getNickname() + ", 请等待他确认.");
                        }

                        String result = mapHelper.json();

                        for (Player client : room.getPlayerList()) {
                            String finalResult1 = result;
                            GlobalVariable.THREAD_POOL.execute(() -> {
                                if (client.getRole() == ClientRole.PLAYER) {
///								ChannelUtils.pushToClient(player, ClientEventCode.CODE_SHOW_POKERS, result);
                                    ClientEventListener.get(ClientEventCode.CODE_SHOW_POKERS).call(client, finalResult1);
                                }
                            });
                        }

//						notifyWatcherPlayPoker(player, result);

                        if (player.getPokers().isEmpty()) {
                            result = MapHelper.newInstance()
                                    .put("winnerType", player.getType())
                                    .json();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            for (Player client : room.getPlayerList()) {
                                String finalResult = result;
                                GlobalVariable.THREAD_POOL.execute(() -> {
                                    if (client.getRole() == ClientRole.PLAYER) {
///									ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_OVER, result);
                                        ClientEventListener.get(ClientEventCode.CODE_GAME_OVER).call(client, finalResult);
                                    }
                                });
                            }

                        } else {
                            if (next.getRole() == ClientRole.PLAYER) {
                                ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_REDIRECT).call(next, result);
                            } else {
                                Objects.requireNonNull(RobotEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY)).call(next, data);
                            }
                        }
                    }
                } else {
///					ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_INVALID, null);
                    ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_INVALID).call(player, null);

                }
            } else {
///				ChannelUtils.pushToClient(player, ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR, null);
                ClientEventListener.get(ClientEventCode.CODE_GAME_POKER_PLAY_ORDER_ERROR).call(player, null);

            }
        }
///		else {
///			ChannelUtils.pushToClient(player, ClientEventCode.CODE_ROOM_PLAY_FAIL_BY_INEXIST, null);
///		}
    }

    /**
     * 通知观战者出牌信息
     *
     * @param player   玩家
     * @param result 出牌信息
     */
    private void notifyWatcherPlayPoker(Player player, String result) {
        ClientEventListener.get(ClientEventCode.CODE_SHOW_POKERS).call(player, result);

    }

    /**
     * 通知观战者游戏结束
     *
     * @param room   房间
     * @param result 出牌信息
     */
    private void notifyWatcherGameOver(Room room, String result) {
//		for (Player watcher : room.getWatcherList()) {
//			ChannelUtils.pushToClient(watcher, ClientEventCode.CODE_GAME_OVER, result);
        ClientEventListener.get(ClientEventCode.CODE_GAME_OVER).call(null, result);
//
//		}
    }
}
