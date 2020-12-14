package com.wuyou.utils;

import com.simplerobot.modules.utils.CodeTemplate;
import com.simplerobot.modules.utils.KQCode;
import com.simplerobot.modules.utils.KQCodeUtils;
import com.simplerobot.modules.utils.MutableKQCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
@Component
public class CQ {
    public static final Map<String, String[]> CONTEXT = new HashMap<>();
    private static String cqPath;
    public static final KQCodeUtils UTILS = KQCodeUtils.getInstance();
    final static CodeTemplate<KQCode> STRING_TEMPLATE = UTILS.getKqCodeTemplate();

    @Value("${cqPath}")
    public static void setCqPath(String cq) {
        cqPath = cq;
    }

    
    public static String at(String qq, String name) {
        final MutableKQCode at = STRING_TEMPLATE.at(qq).mutable();
        at.put("name", name);
        return at + " ";
    }

    public static String at(String qq) {
        return STRING_TEMPLATE.at(qq) + " ";
    }
    public static String startsWithAt(String msg){
        List<String> list = UTILS.split(msg);
        KQCode code = UTILS.getKq(list.get(0),"at");
        if(code!=null) {
            return code.get("qq");
        }
        return "";
    }

    /**
     * 获取所有艾特的QQ号
     *
     */
    public static Set<String> getAts(String msg) {
        final List<KQCode> list = UTILS.getKqs(msg, "at");
        return list.stream().map(item-> item.get("qq")).collect(Collectors.toSet());
    }

    /**
     * 获取所有艾特的QQ号的KQ码
     *
     */
    public static Set<KQCode> getAtKqs(String msg) {
        final List<KQCode> list = UTILS.getKqs(msg, "at");
        return new HashSet<>(list);
    }

    /**
     * 获取第一个艾特的QQ号
     *
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

    public static String getCQPath() {
        return cqPath;
    }

}
