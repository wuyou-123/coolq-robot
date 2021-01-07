package org.nico.ratel.landlords.client.event;

import io.netty.channel.Channel;
import org.nico.noson.Noson;
import org.nico.noson.entity.NoType;
import org.nico.ratel.landlords.client.SimpleClient;
import org.nico.ratel.landlords.entity.Poker;
import org.nico.ratel.landlords.entity.PokerSell;
import org.nico.ratel.landlords.enums.PokerLevel;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.helper.MapHelper;
import org.nico.ratel.landlords.helper.PokerHelper;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.print.SimpleWriter;
import org.nico.ratel.landlords.utils.GetQQUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientEventListener_CODE_GAME_POKER_PLAY extends ClientEventListener{

	@Override
	public void call(Channel channel, String data) {
		String qq = GetQQUtils.getQQ(channel);
		Map<String, Object> map = MapHelper.parser(data);
		
		SimplePrinter.sendNotice(qq, "轮到你出牌了, 你的牌如下: ");
		List<Poker> pokers = Noson.convert(map.get("pokers"), new NoType<List<Poker>>() {});
		SimplePrinter.printPokers(qq, pokers);
		
		
		SimplePrinter.sendNotice(qq, "请输入您想出的牌 (输入 [exit|e] 离开当前房间, 输入 [pass|p] 跳过这一回合, 输入 [view|v] 查看自己的牌)");
		String line = SimpleWriter.write("combination");

		if(line == null){
			SimplePrinter.sendNotice(qq, "错误的输入");
			call(channel, data);
		}else{
			if(line.equalsIgnoreCase("pass") || line.equalsIgnoreCase("p")) {
				pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY_PASS);
			}else if(line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("e")){
				pushToServer(channel, ServerEventCode.CODE_CLIENT_EXIT);
			}else if(line.equalsIgnoreCase("view") || line.equalsIgnoreCase("v")){
				if(! map.containsKey("lastSellPokers") || ! map.containsKey("lastSellClientId")) {
					SimplePrinter.sendNotice(qq, "当前服务器版本不支持此特性，需要超过v1.2.4");
					call(channel, data);
					return;
				}
				Object lastSellPokersObj = map.get("lastSellPokers");
				if(lastSellPokersObj == null || Integer.valueOf(SimpleClient.id).equals(map.get("lastSellClientId"))) {
					SimplePrinter.sendNotice(qq, "Up to you !");
					call(channel, data);
					return;
				}else {
					List<Poker> lastSellPokers = Noson.convert(lastSellPokersObj, new NoType<List<Poker>>() {});
					List<PokerSell> sells = PokerHelper.validSells(PokerHelper.checkPokerType(lastSellPokers), pokers);
					if(sells == null || sells.size() == 0) {
						SimplePrinter.sendNotice(qq, "It is a pity that, there is no winning combination...");
						call(channel, data);
						return;
					}
					for(int i = 0; i < sells.size(); i ++) {
						SimplePrinter.sendNotice(qq, i + 1 + ". " + PokerHelper.textOnlyNoType(sells.get(i).getSellPokers()));
					}
					while(true){
						SimplePrinter.sendNotice(qq, "You can enter index to choose anyone.(enter [back|b] to go back.)");
						line = SimpleWriter.write("choose");
						if(line.equalsIgnoreCase("back") || line.equalsIgnoreCase("b")) {
							call(channel, data);
							return;
						}else {
							try {
								int choose = Integer.valueOf(line);
								if(choose < 1 || choose > sells.size()) {
									SimplePrinter.sendNotice(qq, "The input number must be in the range of 1 to " + sells.size() + ".");
								}else {
									List<Poker> choosePokers = sells.get(choose - 1).getSellPokers();
									List<Character> options = new ArrayList<>();
									for(Poker poker: choosePokers) {
										options.add(poker.getLevel().getAlias()[0]);
									}
									pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY, Noson.reversal(options.toArray(new Character[] {})));
									break;
								}
							}catch(NumberFormatException e) {
								SimplePrinter.sendNotice(qq, "Please input a number.");
							}
						}
					}
				}
				
//				PokerHelper.validSells(lastPokerSell, pokers);
			}else {
				String[] strs = line.split(" ");
				List<Character> options = new ArrayList<>();
				boolean access = true;
				for(int index = 0; index < strs.length; index ++){
					String str = strs[index];
					for(char c: str.toCharArray()) {
						if(c == ' ' || c == '\t') {
						}else {
							if(! PokerLevel.aliasContains(c)) {
								access = false;
								break;
							}else {
								options.add(c);
							}
						}
					}
				}
				if(access){
					pushToServer(channel, ServerEventCode.CODE_GAME_POKER_PLAY, Noson.reversal(options.toArray(new Character[] {})));
				}else{
					SimplePrinter.sendNotice(qq, "Invalid enter");
					
					if(lastPokers != null) {
						SimplePrinter.sendNotice(qq, lastSellClientNickname + "[" + lastSellClientType + "] played:");
						SimplePrinter.printPokers(qq, lastPokers);
					}
					
					call(channel, data);
				}
			}
		}
		
	}

}
