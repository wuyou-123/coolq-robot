package org.nico.ratel.landlords.client.event;

import com.wuyou.entity.Player;
import org.nico.ratel.landlords.entity.Poker;
import com.wuyou.enums.ClientEventCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyou
 */
public abstract class ClientEventListener {

	/**
	 * @param player 玩家
	 * @param data 需要传递的数据
	 */
	public abstract void call(Player player, String data);

	public final static Map<ClientEventCode, ClientEventListener> LISTENER_MAP = new HashMap<>();
	
	private final static String LISTENER_PREFIX = "org.nico.ratel.landlords.client.event.ClientEventListener_";
	
	protected static List<Poker> lastPokers = null;
	protected static String lastSellClientNickname = null;
	protected static String lastSellClientType = null;
	
	protected static void initLastSellInfo() {
		lastPokers = null;
		lastSellClientNickname = null;
		lastSellClientType = null;
	}
	
	public static ClientEventListener get(ClientEventCode code){
		System.out.println("ClientEventListener-----"+code);
		ClientEventListener listener = null;
		try {
			if(ClientEventListener.LISTENER_MAP.containsKey(code)){
				listener = ClientEventListener.LISTENER_MAP.get(code);
			}else{
				String eventListener = LISTENER_PREFIX + code.name();
				Class<?> listenerClass = Class.forName(eventListener);
				listener = (ClientEventListener) listenerClass.newInstance();
				ClientEventListener.LISTENER_MAP.put(code, listener);
			}
			return listener;
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return listener;
	}
	
//	protected ChannelFuture pushToServer(Player player, ServerEventCode code, String datas){
//		return ChannelUtils.pushToServer(player, code, datas);
//	}
//
//	protected ChannelFuture pushToServer(Player player, ServerEventCode code){
//		return pushToServer(player, code, null);
//	}
}
