package com.wuyou.landlords.robot;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.entity.Player;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.entity.PokerSell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * How does the machine decide on a better strategy to win the game
 * 
 * @author nico
 */
public class RobotDecisionMakers {
	
	private static final Map<Integer, AbstractRobotDecisionMakers> DECISION_MAKERS_MAP = new HashMap<>();
	
	public static void init() {
		DECISION_MAKERS_MAP.put(1, new EasyRobotDecisionMakers());
		DECISION_MAKERS_MAP.put(2, new MediumRobotDecisionMakers());
		GlobalVariable.LANDLORDS_PLAYER.clear();
	}
	
	public static boolean contains(int difficultyCoefficient) {
		return DECISION_MAKERS_MAP.containsKey(difficultyCoefficient);
	}
	
	public static PokerSell howToPlayPokers(int difficultyCoefficient, PokerSell lastPokerSell, Player robot){
		return DECISION_MAKERS_MAP.get(difficultyCoefficient).howToPlayPokers(lastPokerSell, robot);
	}
	
	public static boolean howToChooseLandlord(int difficultyCoefficient, List<Poker> leftPokers, List<Poker> rightPokers, List<Poker> myPokers) {
		return DECISION_MAKERS_MAP.get(difficultyCoefficient).howToChooseLandlord(leftPokers, rightPokers, myPokers);
	}
	
}
