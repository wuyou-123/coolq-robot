package com.wuyou.utils;

import com.forte.qqrobot.beans.cqcode.CQCode;
import com.forte.qqrobot.beans.types.CQCodeTypes;
import com.forte.qqrobot.utils.CQCodeUtil;

import java.util.*;

/**
 * @author Administrator<br>
 * 2020年5月3日
 */
public class CQ {
    public static Map<String, String[]> context = new HashMap<String, String[]>();

    public static String cqPath;

    public static String at(String qq) {
        return CQCodeUtil.build().getCQCode_At(qq).toString() + " ";
    }

    public static Set<String> getAts(String msg) {
        List<CQCode> list = CQCodeUtil.build().getCQCodeFromMsgByType(msg, CQCodeTypes.at);
        Set<String> set = new HashSet<String>();
        for (CQCode CQCode : list) {
            set.add(CQCode.get("qq"));
        }
        return set;
    }

    public static String getAt(String code) {
        List<CQCode> list = CQCodeUtil.build().getCQCodeFromMsgByType(code, "at");
        if (list == null || list.size() == 0)
            return null;
        return list.get(0).get("qq");
    }

    public static String getCQPath() {
        return cqPath;
    }

}
