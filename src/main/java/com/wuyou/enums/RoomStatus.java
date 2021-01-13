package com.wuyou.enums;

public enum RoomStatus{
	/**
	 * 房间状态
	 */
	BLANK("空闲"),
	
	WAIT("等待"),
	
	STARTING("开始"),

	STOPPED("已结束"),
	
	
	;
	
	private String msg;

	RoomStatus(String msg) {
		this.msg = msg;
	}

	public final String getMsg() {
		return msg;
	}

	public final void setMsg(String msg) {
		this.msg = msg;
	}
	
}
