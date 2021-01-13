package com.wuyou.enums;

/**
 * @author wuyou
 */
public enum FaceEnum {
    /**
     * 表情对应id
     */
    Face1(1, "/撇嘴"), Face2(2, "/色"), Face3(3, "/发呆"), Face4(4, "/得意"),
    Face5(5, "/流泪"), Face6(6, "/害羞"), Face7(7, "/住嘴"), Face8(8, "/瞌睡"),
    Face9(9, "/大哭"), Face10(10, "/尴尬"), Face11(11, "/发怒"), Face12(12, "/调皮"),
    Face13(13, "/呲牙"), Face14(14, "/微笑"), Face15(15, "/撇嘴"), Face16(16, "/得意"),
    Face17(17, "/口罩护体"), Face18(18, "/抓狂"), Face19(19, "/呕吐"), Face20(20, "/偷笑"),
    Face21(21, "/可爱"), Face22(22, "/白眼"), Face23(23, "/傲慢"), Face24(24, "/饥饿"),
    Face25(25, "/困"), Face26(26, "/惊吓"), Face27(27, "/流汗"), Face28(28, "/憨笑"),
    Face29(29, "/大兵"), Face30(30, "/奋斗"), Face31(31, "/咒骂"), Face32(32, "/疑问"),
    Face33(33, "/嘘"), Face34(34, "/晕"), Face35(35, "/折磨"), Face36(36, "/衰"),
    Face37(37, "/骷髅"), Face38(38, "/敲打"), Face39(39, "/再见"), Face41(41, "/发抖"),
    Face42(42, "/爱情"), Face43(43, "/跳跳"), Face46(46, "/猪头"), Face47(47, "/再见"),
    Face48(48, "/再见"), Face49(49, "/拥抱"), Face53(53, "/蛋糕"), Face54(54, "/闪电"),
    Face55(55, "/炸弹"), Face56(56, "/刀"), Face57(57, "/足球"), Face59(59, "/便便"),
    Face60(60, "/咖啡"), Face61(61, "/米饭"), Face62(62, "/药"), Face63(63, "/玫瑰"),
    Face64(64, "/凋谢"), Face65(65, "/示爱"), Face66(66, "/爱心"), Face67(67, "/心碎"),
    Face69(69, "/礼物"), Face74(74, "/太阳"), Face75(75, "/月亮"), Face76(76, "/强"),
    Face77(77, "/弱"), Face78(78, "/握手"), Face79(79, "/胜利"), Face86(86, "/怄火"),
    Face89(89, "/西瓜"), Face96(96, "/冷汗"), Face97(97, "/擦汗"), Face98(98, "/抠鼻"),
    Face99(99, "/鼓掌"), Face100(100, "/糗大了"), Face101(101, "/坏笑"), Face102(102, "/左哼哼"),
    Face103(103, "/右哼哼"), Face104(104, "/哈欠"), Face105(105, "/鄙视"), Face106(106, "/委屈"),
    Face107(107, "/快哭了"), Face108(108, "/奸笑"), Face109(109, "/亲亲"), Face110(110, "/吓"),
    Face111(111, "/可怜"), Face112(112, "/菜刀"), Face113(113, "/啤酒"), Face114(114, "/篮球"),
    Face115(115, "/乒乓球"), Face116(116, "/示爱"), Face117(117, "/瓢虫"), Face118(118, "/抱拳"),
    Face119(119, "/勾引"), Face120(120, "/拳头"), Face121(121, "/差劲"), Face122(122, "/爱你"),
    Face123(123, "/NO"), Face124(124, "/OK"), Face125(125, "/转圈"), Face126(126, "/磕头"),
    Face127(127, "/回头"), Face128(128, "/跳绳"), Face129(129, "/挥手"), Face130(130, "/激动"),
    Face131(131, "/街舞"), Face132(132, "/献吻"), Face133(133, "/左太极"), Face134(134, "/右太极"),
    Face135(135, "/招财猫"), Face136(136, "/双喜"), Face137(137, "/鞭炮"), Face138(138, "/灯笼"),
    Face140(140, "/K歌"), Face144(144, "/喝彩"), Face145(145, "/蜡烛"), Face146(146, "/爆筋"),
    Face147(147, "/棒棒糖"), Face148(148, "/奶瓶"), Face151(151, "/飞机"), Face158(158, "/钞票"),
    Face168(168, "/药"), Face169(169, "/手枪"), Face171(171, "/茶"), Face172(172, "/眨眼睛"),
    Face173(173, "/泪奔"), Face174(174, "/无奈"), Face175(175, "/卖萌"), Face176(176, "/小纠结"),
    Face177(177, "/喷血"), Face178(178, "/斜眼笑"), Face179(179, "/doge"), Face180(180, "/惊喜"),
    Face181(181, "/骚扰"), Face182(182, "/笑哭"), Face183(183, "/我最美"), Face184(184, "/螃蟹"),
    Face185(185, "/羊驼"), Face186(186, "/栗子"), Face187(187, "/幽灵"), Face188(188, "/鸡蛋"),
    Face190(190, "/菊花"), Face191(191, "/香皂"), Face192(192, "/红包"), Face193(193, "/大笑"),
    Face194(194, "/不开心"), Face197(197, "/冷漠"), Face198(198, "/呃"), Face199(199, "/好棒"),
    Face200(200, "/拜托"), Face201(201, "/点赞"), Face202(202, "/无聊"), Face203(203, "/托脸"),
    Face204(204, "/吃"), Face205(205, "/送花"), Face206(206, "/害怕"), Face207(207, "/花痴"),
    Face208(208, "/小样儿"), Face209(209, "/拜托"), Face210(210, "/飙泪"), Face211(211, "/我不看"),
    Face212(212, "/托腮"), Face213(213, "");

    private final int id;
    private final String string;

    FaceEnum(int id, String string) {
        this.id = id;
        this.string = string;
    }

    public static String getString(int id) {
        for (FaceEnum value : values()) {
            if (value.id == id) {
                return value.string;
            }
        }
        return "";
    }

    public static String getString(String idString) {
        int id = Integer.parseInt(idString);
        return getString(id);
    }

    @Override
    public String toString() {
        return string;
    }
}
