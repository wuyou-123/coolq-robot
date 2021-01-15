package com.wuyou.landlords.server.robot;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface RobotEventListener {

	String LISTENER_PREFIX = "com.wuyou.landlords.server.robot.RobotEventListener_";
	
	Map<ClientEventCode, RobotEventListener> LISTENER_MAP = new HashMap<>();
	
	void call(Player robot, String data);

	@SuppressWarnings("unchecked")
	static RobotEventListener get(ClientEventCode code) {
		System.out.println("RobotEventListener-----"+code);
		RobotEventListener listener = null;
		try {
			if(RobotEventListener.LISTENER_MAP.containsKey(code)){
				listener = RobotEventListener.LISTENER_MAP.get(code);
			}else{
				String eventListener = LISTENER_PREFIX + code.name();
				Class<RobotEventListener> listenerClass = (Class<RobotEventListener>) Class.forName(eventListener);
				try {
					listener = listenerClass.getDeclaredConstructor().newInstance();
				} catch (NoSuchMethodException | InvocationTargetException e) {
					e.printStackTrace();
				}
				RobotEventListener.LISTENER_MAP.put(code, listener);
			}
			return listener;
		}catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
