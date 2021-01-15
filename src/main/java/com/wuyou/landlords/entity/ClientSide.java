package com.wuyou.landlords.entity;

import com.wuyou.entity.Player;
import com.wuyou.enums.ClientRole;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.ClientType;

import java.util.List;

public class ClientSide{

	private String id;
	
	private String roomId;
	
	private String nickname;
	
	private List<Poker> pokers;
	
	private ClientStatus status;
	
	private ClientRole role;
	
	private ClientType type;
	
	private ClientSide next;
	
	private ClientSide pre;
	
	private transient Player player;
	
	public ClientSide() {}

	public ClientSide(String id, ClientStatus status, Player player) {
		this.id = id;
		this.status = status;
		this.player = player;
	}
	
	public void init() {
		roomId = "0";
		pokers = null;
		status = ClientStatus.TO_CHOOSE;
		type = null;
		next = null;
		pre = null;
	}

	public final ClientRole getRole() {
		return role;
	}

	public final void setRole(ClientRole role) {
		this.role = role;
	}

	public final String getNickname() {
		return nickname;
	}

	public final void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public final Player getChannel() {
		return player;
	}

	public final void setChannel(Player player) {
		this.player = player;
	}

	public final String getRoomId() {
		return roomId;
	}

	public final void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public final List<Poker> getPokers() {
		return pokers;
	}

	public final void setPokers(List<Poker> pokers) {
		this.pokers = pokers;
	}

	public final ClientStatus getStatus() {
		return status;
	}

	public final void setStatus(ClientStatus status) {
		this.status = status;
	}

	public final ClientType getType() {
		return type;
	}

	public final void setType(ClientType type) {
		this.type = type;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final ClientSide getNext() {
		return next;
	}

	public final void setNext(ClientSide next) {
		this.next = next;
	}

	public final ClientSide getPre() {
		return pre;
	}

	public final void setPre(ClientSide pre) {
		this.pre = pre;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ClientSide other = (ClientSide) obj;
		return id.equals(other.id);
	}

	@Override
	public String toString() {
		return "ClientSide{" +
				"id='" + id + '\'' +
				", roomId='" + roomId + '\'' +
				", nickname='" + nickname + '\'' +
				'}';
	}
}
