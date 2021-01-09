package org.nico.ratel.landlords.entity;

import org.nico.ratel.landlords.enums.RoomStatus;
import org.nico.ratel.landlords.enums.RoomType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class Room{

	private String id;
	
	private String roomOwner;
	
	private RoomStatus status;
	
	private RoomType type;
	
	private Map<String, ClientSide> clientSideMap;
	
	private LinkedList<ClientSide> clientSideList;
	
	private String landlordId = "-1";
	
	private List<Poker> landlordPokers;
	
	private PokerSell lastPokerShell;
	
	private String lastSellClient = "-1";
	
	private String currentSellClient = "-1";
	
	private int difficultyCoefficient;
	
	private long lastFlushTime;
	
	private long createTime;

	private String firstSellClient;

	/** 观战者列表 */
	private List<ClientSide> watcherList = new ArrayList<>(5);
	
	public Room() {
	}

	public Room(String id) {
		this.id = id;
		this.clientSideMap = new ConcurrentSkipListMap<>();
		this.clientSideList = new LinkedList<>();
		this.status = RoomStatus.BLANK;
	}

	public final long getCreateTime() {
		return createTime;
	}

	public final void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public final int getDifficultyCoefficient() {
		return difficultyCoefficient;
	}

	public final void setDifficultyCoefficient(int difficultyCoefficient) {
		this.difficultyCoefficient = difficultyCoefficient;
	}

	public final RoomType getType() {
		return type;
	}

	public final void setType(RoomType type) {
		this.type = type;
	}

	public final PokerSell getLastPokerShell() {
		return lastPokerShell;
	}

	public final void setLastPokerShell(PokerSell lastPokerShell) {
		this.lastPokerShell = lastPokerShell;
	}

	public final String getCurrentSellClient() {
		return currentSellClient;
	}

	public final void setCurrentSellClient(String currentSellClient) {
		this.currentSellClient = currentSellClient;
	}

	public long getLastFlushTime() {
		return lastFlushTime;
	}

	public void setLastFlushTime(long lastFlushTime) {
		this.lastFlushTime = lastFlushTime;
	}

	public String getLastSellClient() {
		return lastSellClient;
	}

	public void setLastSellClient(String lastSellClient) {
		this.lastSellClient = lastSellClient;
	}

	public String getLandlordId() {
		return landlordId;
	}

	public void setLandlordId(String landlordId) {
		this.landlordId = landlordId;
	}

	public LinkedList<ClientSide> getClientSideList() {
		return clientSideList;
	}

	public void setClientSideList(LinkedList<ClientSide> clientSideList) {
		this.clientSideList = clientSideList;
	}

	public List<Poker> getLandlordPokers() {
		return landlordPokers;
	}

	public void setLandlordPokers(List<Poker> landlordPokers) {
		this.landlordPokers = landlordPokers;
	}

	public final String getRoomOwner() {
		return roomOwner;
	}

	public final void setRoomOwner(String roomOwner) {
		this.roomOwner = roomOwner;
	}

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final RoomStatus getStatus() {
		return status;
	}

	public final void setStatus(RoomStatus status) {
		this.status = status;
	}

	public final Map<String, ClientSide> getClientSideMap() {
		return clientSideMap;
	}

	public final void setClientSideMap(Map<String, ClientSide> clientSideMap) {
		this.clientSideMap = clientSideMap;
	}

	public String getFirstSellClient() {
		return firstSellClient;
	}

	public void setFirstSellClient(String firstSellClient) {
		this.firstSellClient = firstSellClient;
	}

	public List<ClientSide> getWatcherList() {
		return watcherList;
	}
}
