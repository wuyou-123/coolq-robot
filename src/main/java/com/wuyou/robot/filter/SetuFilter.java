package com.wuyou.robot.filter;

import com.wuyou.utils.CQ;
import love.forte.catcode.Neko;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MsgGet;
import love.forte.simbot.filter.FilterData;
import love.forte.simbot.filter.ListenerFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取现在机器人是否开机
 *
 * @author Administrator<br>
 * 2020年5月2日
 */
@Component
public class SetuFilter {
    @Component("setuImage")
    public static class SetuImage implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                List<Neko> list = CQ.getKq(message, "image");
                for (Neko neko : list) {
                    String nekoStr = neko.get("file");
                    if(nekoStr != null) {
                        return "{463FBD38-F6A0-BD4E-D008-D84D26DCE538}.mirai".contains(nekoStr);
                    }
                }
            }
            return false;
        }
    }

    @Component("setu")
    public static class Setu implements ListenerFilter {

        @Override
        public boolean test(@NotNull FilterData data) {
            List<String> list = new ArrayList<>();
            list.add("r18色图");
            list.add("r18涩图");
            list.add("来点r18色图");
            list.add("来点r18涩图");
            list.add("来一份r18色图");
            list.add("来一份r18涩图");
            list.add("来份r18色图");
            list.add("来份r18涩图");
            list.add("色图");
            list.add("涩图");
            list.add("来点色图");
            list.add("来点涩图");
            list.add("来一份色图");
            list.add("来一份涩图");
            list.add("来份色图");
            list.add("来份涩图");
            list.add("来点好看的");
            list.add("来点好康的");
            MsgGet msgget = data.getMsgGet();
            if (msgget instanceof GroupMsg) {
                String message = ((GroupMsg) msgget).getMsg().trim();
                String msg = CQ.UTILS.remove(message, true, true);
                if (list.contains(msg.toLowerCase())) {
                    return true;
                }
                return message.contains("色") && message.contains("图") && message.length() < 10;
            }
            return false;
        }
    }
}
