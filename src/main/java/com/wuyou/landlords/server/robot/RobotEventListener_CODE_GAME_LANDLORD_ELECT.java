package com.wuyou.landlords.server.robot;

import com.wuyou.entity.Player;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.landlords.entity.Poker;
import com.wuyou.landlords.entity.Room;
import com.wuyou.landlords.helper.PokerHelper;
import com.wuyou.landlords.helper.TimeHelper;
import com.wuyou.landlords.robot.RobotDecisionMakers;
import com.wuyou.landlords.server.event.ServerEventListener;
import com.wuyou.utils.GlobalVariable;

import java.util.ArrayList;
import java.util.List;

public class RobotEventListener_CODE_GAME_LANDLORD_ELECT implements RobotEventListener{

	@Override
	public void call(Player robot, String data) {
		GlobalVariable.THREAD_POOL.execute(() -> {
			Room room = GlobalVariable.getRoom(robot.getRoomId());

			List<Poker> landlordPokers = new ArrayList<>(20);
			landlordPokers.addAll(robot.getPokers());
			landlordPokers.addAll(room.getLandlordPokers());

			List<Poker> leftPokers = new ArrayList<>(17);
			leftPokers.addAll(robot.getPre().getPokers());

			List<Poker> rightPokers = new ArrayList<>(17);
			rightPokers.addAll(robot.getNext().getPokers());

			PokerHelper.sortPoker(landlordPokers);
			PokerHelper.sortPoker(leftPokers);
			PokerHelper.sortPoker(rightPokers);

			TimeHelper.sleep(300);
			
			ServerEventListener.get(ServerEventCode.CODE_GAME_LANDLORD_ELECT).call(robot, String.valueOf(RobotDecisionMakers.howToChooseLandlord(room.getDifficultyCoefficient(), leftPokers, rightPokers, landlordPokers)));
		});
	}

}
