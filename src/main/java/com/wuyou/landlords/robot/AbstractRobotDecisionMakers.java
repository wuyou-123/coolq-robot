package com.wuyou.landlords.robot;

import com.wuyou.entity.Player;
import com.wuyou.landlords.entity.PokerSell;
import com.wuyou.landlords.entity.Poker;

import java.util.List;

/** 
 * 
 * @author nico
 * @version createTime：2018年11月15日 上午12:12:15
 */

public abstract	class AbstractRobotDecisionMakers {

	public abstract PokerSell howToPlayPokers(PokerSell lastPokerSell, Player robot);
	
	public abstract boolean howToChooseLandlord(List<Poker> leftPokers, List<Poker> rightPokers, List<Poker> myPokers);
}
