package org.nico.ratel.landlords.server.robot;

import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.enums.ClientEventCode;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface RobotEventListener {

	final static String LISTENER_PREFIX = "org.nico.ratel.landlords.server.robot.RobotEventListener_";
	
	public final static Map<ClientEventCode, RobotEventListener> LISTENER_MAP = new HashMap<>();
	
	public void call(ClientSide robot, String data);

	@SuppressWarnings("unchecked")
	public static RobotEventListener get(ClientEventCode code) {
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
		return listener;
	}
	
}
