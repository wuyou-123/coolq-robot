package com.wuyou.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.simplerobot.modules.utils.CodeTemplate;
import com.simplerobot.modules.utils.KQCode;
import com.simplerobot.modules.utils.KQCodeUtils;
import com.simplerobot.modules.utils.MutableKQCode;
import org.nico.ratel.landlords.entity.Poker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Component
public class CQ {
    public static final Map<String, String[]> CONTEXT = new HashMap<>();
    public static final KQCodeUtils UTILS = KQCodeUtils.getInstance();
    final static CodeTemplate<KQCode> STRING_TEMPLATE = UTILS.getKqCodeTemplate();
    private static String cqPath;
    private static String pokerPath;

    public static void setCqPath(String cq) {
        cqPath = cq;
    }

    public static void setPokerPath(String poker) {
        pokerPath = poker;
    }

    public static String at(String qq, String name) {
        final MutableKQCode at = STRING_TEMPLATE.at(qq).mutable();
        at.put("name", name);
        return at + " ";
    }

    public static String at(String qq) {
        return STRING_TEMPLATE.at(qq) + " ";
    }

    public static String startsWithAt(String msg) {
        List<String> list = UTILS.split(msg);
        KQCode code = UTILS.getKq(list.get(0), "at");
        if (code != null) {
            return code.get("qq");
        }
        return "";
    }

    /**
     * 获取所有艾特的QQ号
     */
    public static Set<String> getAts(String msg) {
        final List<KQCode> list = UTILS.getKqs(msg, "at");
        return list.stream().map(item -> item.get("qq")).collect(Collectors.toSet());
    }

    /**
     * 获取所有艾特的QQ号的KQ码
     */
    public static Set<KQCode> getAtKqs(String msg) {
        final List<KQCode> list = UTILS.getKqs(msg, "at");
        return new HashSet<>(list);
    }

    /**
     * 获取第一个艾特的QQ号
     */
    public static String getAt(String msg) {
        final List<KQCode> list = UTILS.getKqs(msg, "at");
        if (list.size() == 0) {
            return null;
        }
        return list.get(0).get("qq");
    }

    public static List<KQCode> getKq(String msg, String type) {
        return UTILS.getKqs(msg, type);
    }

    public static KQCode getFace(String id) {
        return STRING_TEMPLATE.face(id);
    }

    public static KQCode getRecord(String path) {
        return STRING_TEMPLATE.record(path);
    }

    public static KQCode getImage(String path) {
        return STRING_TEMPLATE.image(path);
    }

    public static KQCode getMusic(String music) {

        String resp = HttpUtils.get("http://music.163.com/api/search/get/web?type=1&s=" + music).getResponse();
        JSONObject json = JSONObject.parseObject(resp);
        Map<String, String> map = new HashMap<>();
        JSONArray jsonArray = json.getJSONObject("result").getJSONArray("songs");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        String id = jsonObject.getString("id");
        JSONArray artistsJson = jsonObject.getJSONArray("artists");
        List<String> artistList = artistsJson.stream().map(item -> {
            JSONObject object = (JSONObject) item;
            return object.getString("name");
        }).collect(Collectors.toList());
        String artists = String.join("&", artistList);
        String song = "https://api.imjad.cn/cloudmusic/?type=song&id=" + id;
        String url = JSONObject.parseObject(HttpUtils.get(song).getResponse()).getJSONArray("data").getJSONObject(0).getString("url");
        String detail = "https://api.imjad.cn/cloudmusic/?type=detail&id=" + id;
        String preview = JSONObject.parseObject(HttpUtils.get(detail).getResponse()).getJSONArray("songs").getJSONObject(0).getJSONObject("al").getString("picUrl");
        String jumpUrl = "https://music.163.com/#/song?id=" + id;
        String title = jsonObject.getString("name");
        String code = "{" +
                "  \"app\": \"com.tencent.structmsg\"," +
                "  \"desc\": \"音乐\"," +
                "  \"view\": \"music\"," +
                "  \"ver\": \"0.0.0.1\"," +
                "  \"prompt\": \"[分享]" + title + "\"," +
                "  \"appID\": \"\"," +
                "  \"sourceName\": \"\"," +
                "  \"actionData\": \"\"," +
                "  \"actionData_A\": \"\"," +
                "  \"sourceUrl\": \"\"," +
                "  \"meta\": {" +
                "    \"music\": {" +
                "      \"preview\": \"" + preview + "\"," +
                "      \"app_type\": 1," +
                "      \"musicUrl\": \"" + url + "\"," +
                "      \"title\": \"" + title + "\"," +
                "      \"jumpUrl\": \"" + jumpUrl + "\"," +
                "      \"source_url\": \"\"," +
                "      \"android_pkg_name\": \"\"," +
                "      \"source_icon\": \"\"," +
                "      \"appid\": 100495085," +
                "      \"sourceMsgId\": \"0\"," +
                "      \"action\": \"\"," +
                "      \"tag\": \"网易云音乐\"," +
                "      \"desc\": \"" + artists + "\"" +
                "    }" +
                "  }," +
                "  \"config\": {" +
                "    \"forward\": true," +
                "    \"ctime\": " + (System.currentTimeMillis() / 1000) + "," +
                "    \"type\": \"normal\"," +
                "    \"token\": \"\"" +
                "  }," +
                "  \"text\": \"\"," +
                "  \"sourceAd\": \"\"," +
                "  \"extra\": \"\"" +
                "}";
        map.put("content", code);
        return CQ.UTILS.toKq("app", map);
    }

    public static String getPoker(List<Poker> pokers) {
        try {
            List<String> pokerList = new ArrayList<>();
            for (Poker poker : pokers) {
                String a = poker.getLevel().getName().replace("A", "1");
                String b;
                switch (poker.getType().toString()) {
                    case "SPADE":
                        b = "a";
                        break;
                    case "HEART":
                        b = "b";
                        break;
                    case "CLUB":
                        b = "c";
                        break;
                    case "DIAMOND":
                        b = "d";
                        break;
                    default:
                        b = "e";
                }
                pokerList.add((b + a).toLowerCase(Locale.ROOT).replace("10", "0"));
            }
            char[] sort = new char[]{'x', 's', '2', '1', 'k', 'q', 'j', '0', '9', '8', '7', '6', '5', '4', '3'};
            char[] sort2 = new char[]{'e', 'a', 'b', 'c', 'd'};
            pokerList.sort((a, b) -> {

                int aIndex = -1;
                int bIndex = -1;
                for (int i = 0; i < sort.length; i++) {
                    if (a.split("")[1].charAt(0) == (sort[i])) {
                        aIndex = i;
                    }
                    if (b.split("")[1].charAt(0) == (sort[i])) {
                        bIndex = i;
                    }
                }
                if (aIndex == bIndex) {
                    for (int i = 0; i < sort2.length; i++) {
                        if (a.split("")[0].charAt(0) == (sort2[i])) {
                            aIndex = i;
                        }
                        if (b.split("")[0].charAt(0) == (sort2[i])) {
                            bIndex = i;
                        }
                    }
                }
                return aIndex - bIndex;
            });
            String pokerStr = pokerList.toString().replace(" ", "").replace(",", "_");
            pokerStr = pokerStr.substring(1, pokerStr.length() - 1);
            File pokerDir = new File(pokerPath + "poker_comp/");
            if (!pokerDir.exists()) {
                if (pokerDir.mkdirs()) {
                    System.out.println("创建文件夹成功");
                }
            }
            File pokerFile = new File(pokerPath + "poker_comp/" + pokerStr + ".png");
            if (pokerFile.exists()) {
                return getImage(pokerFile.toString()).toString();
            }
            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(pokerPath + "generatePoker.py");
            command.add(pokerPath + "poker/");
            command.add(pokerFile.toString());
            command.addAll(pokerList);
//            command.forEach(item->{
//                System.out.print(item+" ");
//            });
//            System.out.println();
            Process proc = Runtime.getRuntime().exec(command.toArray(new String[]{}));
            proc.waitFor();
            return getImage(pokerFile.toString()).toString();
        } catch (Exception e) {
            return "";
        }

    }

    public static String getCQPath() {
        return cqPath;
    }

    @Value("${cqPath}")
    public void setCQ(String cq) {
        setCqPath(cq);
    }

    @Value("${pokerPath}")
    public void setPoker(String poker) {
        setPokerPath(poker);
    }

}
