package org.nico.ratel.landlords.server.robot;

import com.wuyou.entity.Player;
import com.wuyou.enums.SellType;
import com.wuyou.enums.ServerEventCode;
import com.wuyou.utils.GlobalVariable;
import org.nico.noson.Noson;
import org.nico.ratel.landlords.entity.PokerSell;
import org.nico.ratel.landlords.entity.Room;
import org.nico.ratel.landlords.helper.TimeHelper;
import org.nico.ratel.landlords.robot.RobotDecisionMakers;
import org.nico.ratel.landlords.server.event.ServerEventListener;

public class RobotEventListener_CODE_GAME_POKER_PLAY implements RobotEventListener{

	@Override
	public void call(Player robot, String data) {
		GlobalVariable.THREAD_POOL.execute(() -> {
			Room room = GlobalVariable.getRoom(robot.getRoomId());

			PokerSell lastPokerSell = null;
			PokerSell pokerSell;
			if(!room.getLastSellClient().equals(robot.getId())) {
				lastPokerSell = room.getLastPokerShell();
				pokerSell = RobotDecisionMakers.howToPlayPokers(room.getDifficultyCoefficient(), lastPokerSell, robot);
			}else {
				pokerSell = RobotDecisionMakers.howToPlayPokers(room.getDifficultyCoefficient(), null, robot);
			}

			//				SimplePrinter.serverLog("Robot monitoring[room:" + room.getId() + "]");
			//				SimplePrinter.serverLog("last  sell  -> " + lastPokerSell.toString());
			//				SimplePrinter.serverLog("robot sell  -> " + pokerSell.toString());
			//				SimplePrinter.serverLog("robot poker -> " + PokerHelper.textOnlyNoType(robot.getPokers()));

			TimeHelper.sleep(300);
			
			if(pokerSell == null || pokerSell.getSellType() == SellType.ILLEGAL) {
				ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY_PASS).call(robot, data);
			}else {
				Character[] cs = new Character[pokerSell.getSellPokers().size()];
				for(int index = 0; index < cs.length; index ++) {
					cs[index] = pokerSell.getSellPokers().get(index).getLevel().getAlias()[0];
				}
				ServerEventListener.get(ServerEventCode.CODE_GAME_POKER_PLAY).call(robot, Noson.reversal(cs));
			}
		});
	}
}
