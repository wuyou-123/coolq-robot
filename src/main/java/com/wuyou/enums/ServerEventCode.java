package com.wuyou.enums;

import java.io.Serializable;

public enum ServerEventCode implements Serializable{

	CODE_CLIENT_EXIT("玩家退出"),
	
	CODE_CLIENT_OFFLINE("玩家离线"),

	CODE_CLIENT_HEAD_BEAT("不出"),
	
	CODE_ROOM_CREATE("创建房间"),

	CODE_ROOM_JOIN("加入房间"),
	
	CODE_GAME_STARTING("游戏开始"),
	
	CODE_GAME_LANDLORD_ELECT("抢地主"),
	
	CODE_GAME_POKER_PLAY("出牌环节"),
	
	CODE_GAME_POKER_PLAY_REDIRECT("出牌重定向"),
	
	CODE_GAME_POKER_PLAY_PASS("不出"),

	CODE_CLIENT_READY("玩家准备");
	
	private String msg;

	ServerEventCode(String msg) {
		this.msg = msg;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}
	
}
