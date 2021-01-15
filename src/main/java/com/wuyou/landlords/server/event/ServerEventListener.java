package com.wuyou.landlords.server.event;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface ServerEventListener {

	void call(Player client, String data);

	Map<ServerEventCode, ServerEventListener> LISTENER_MAP = new HashMap<>();
	
	String LISTENER_PREFIX = "com.wuyou.landlords.server.event.ServerEventListener_";
	
	@SuppressWarnings("unchecked")
	static ServerEventListener get(ServerEventCode code){
//		System.out.println("ServerEventListener-----"+code);
		ServerEventListener listener = null;
		try {
			if(ServerEventListener.LISTENER_MAP.containsKey(code)){
				listener = ServerEventListener.LISTENER_MAP.get(code);
			}else{
				String eventListener = LISTENER_PREFIX + code.name();
				Class<ServerEventListener> listenerClass = (Class<ServerEventListener>) Class.forName(eventListener);
				try {
					listener = listenerClass.getDeclaredConstructor().newInstance();
				} catch (InvocationTargetException | NoSuchMethodException e) {
					e.printStackTrace();
				}
				ServerEventListener.LISTENER_MAP.put(code, listener);
			}
			return listener;
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return listener;
	}
	
}
