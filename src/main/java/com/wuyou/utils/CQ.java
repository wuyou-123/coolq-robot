package com.wuyou.utils;

import com.simplerobot.modules.utils.*;

import java.util.*;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
public class CQ {
    public static Map<String, String[]> context = new HashMap<>();

    public static String cqPath;
    public final static KQCodeUtils utils = KQCodeUtils.getInstance();
    final static CodeTemplate<KQCode> stringTemplate = utils.getKqCodeTemplate();

    public static String at(String qq, String name) {
        final MutableKQCode at = stringTemplate.at(qq).mutable();
        at.put("name", name);
        return at + " ";
    }

    public static String at(String qq) {
        return stringTemplate.at(qq) + " ";
    }

    /**
     * 获取所有艾特的QQ号
     *
     * @param msg
     * @return
     */
    public static Set<String> getAts(String msg) {
        final List<KQCode> list = utils.getKqs(msg, "at");
        System.out.println(list);
        Set<String> set = new HashSet<>();
        for (KQCode KQCode : list) {
            set.add(KQCode.get("qq"));
        }
        return set;
    }

    /**
     * 获取第一个艾特的QQ号
     *
     * @param msg
     * @return
     */
    public static String getAt(String msg) {
        final List<KQCode> list = utils.getKqs(msg, "at");
        if (list.size() == 0)
            return null;
        return list.get(0).get("qq");
    }

    public static List<KQCode> getKq(String msg, String type) {
        return utils.getKqs(msg, type);
    }

    public static KQCode getFace(String id) {
        return stringTemplate.face(id);
    }

    public static KQCode getRecord(String id) {
        return stringTemplate.record(id);
    }

    public static String getCQPath() {
        return cqPath;
    }

}
