package org.nico.ratel.landlords.print;

import com.forte.qqrobot.BotRuntime;
import com.forte.qqrobot.anno.depend.Beans;
import com.forte.qqrobot.bot.BotManager;
import com.wuyou.utils.CQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nico.ratel.landlords.entity.Poker;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Beans
@Component
public class SimplePrinter {

    private final static SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Log log = LogFactory.getLog(SimplePrinter.class);
    private static final BotManager botManager;
    public static int pokerDisplayFormat = 0;

    static {
        botManager = BotRuntime.getRuntime().getBotManager();
    }

//    @Autowired
//    @Depend
//    public void setBotManager(BotManager manager) {
//        setManager(manager);
//    }
//    public static void setManager(final BotManager manager) {
//        System.out.println("注入: " + manager);
//        botManager = manager;
//    }

    public static void printPokers(String qq, List<Poker> pokers) {
//        System.out.println(PokerHelper.printPoker(pokers));
        String imageCode = CQ.getPoker(pokers);
        System.out.println(imageCode);
        sendNotice(qq, imageCode);
//        botManager.defaultBot().getSender().SENDER.sendPrivateMsg(qq, imageCode);
    }

    public static void sendNotice(String qq, String msg) {
        System.out.println("向" + qq + "发送消息: " + msg);
        if (qq == null || msg == null || msg.isEmpty()) {
            return;
        }
        botManager.defaultBot().getSender().SENDER.sendPrivateMsg(qq, msg);
    }

    public static void printNotice(String msg) {
        System.out.println(msg);
    }

    public static void printNotice(String msgKey, String locale) {
        //TODO : read locale
        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
        map.put("english", new HashMap<String, String>());
        map.get("eng").put("caterpillar", "caterpillar's message!!");

        System.out.println(map.get(locale).get(msgKey));
    }

    public static void serverLog(String msg) {
//		QQLogLang logLang = new QQLogLang("landlords");
//		logLang.info(msg);
//		log.(msg);
        System.out.println(FORMAT.format(new Date()) + " landlords: " + msg);
    }


}
