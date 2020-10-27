package com.wuyou.utils;

import com.simplerobot.modules.utils.CodeTemplate;
import com.simplerobot.modules.utils.KQCode;
import com.simplerobot.modules.utils.KQCodeUtils;
import com.simplerobot.modules.utils.MutableKQCode;

import java.util.*;
import java.util.stream.Collectors;

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
     */
    public static Set<String> getAts(String msg) {
        final List<KQCode> list = utils.getKqs(msg, "at");
        return list.stream().map(item-> item.get("qq")).collect(Collectors.toSet());
    }

    /**
     * 获取所有艾特的QQ号的KQ码
     *
     */
    public static Set<KQCode> getAtKqs(String msg) {
        final List<KQCode> list = utils.getKqs(msg, "at");
        return new HashSet<>(list);
    }

    /**
     * 获取第一个艾特的QQ号
     *
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

    public static KQCode getRecord(String path) {
        return stringTemplate.record(path);
    }
    public static KQCode getImage(String path) {
        return stringTemplate.image(path);
    }

    public static String getCQPath() {
        return cqPath;
    }

}
