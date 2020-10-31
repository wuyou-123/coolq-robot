package com.wuyou.entity;

import com.forte.qqrobot.beans.messages.types.PowerType;
import com.forte.qqrobot.beans.messages.types.SexType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

/**
 * 群成员实体类
 *
 * @author wuyou
 */
@Getter
@Setter
public class GroupMemberEntity {
    /**
     * QQ号
     */
    private Long uin;
    /**
     * 权限
     */
    private PowerType role;
    /**
     * 性别
     */
    private SexType sex;
    /**
     * 入群时间
     */
    private Date joinTime;
    /**
     * 最后一次发言时间
     */
    private Date lastSpeakTime;
    /**
     * 群聊积分积分
     */
    private int point;
    /**
     * 群聊等级
     */
    private int level;
    /**
     * 群名片
     */
    private String card;
    /**
     * QQ昵称
     */
    private String nick;
    /**
     * Q龄
     */
    private int qage;
    /**
     * (下面这三个我也不知道是什么)
     */
    private String tags;
    private int flag;
    private int rm;

    /**
     * 获取名片或者昵称,如果名片为空则返回昵称
     *
     * @return 名片或者昵称, 如果名片为空则返回昵称
     */
    public String getCardOrNick() {
        if (card != null && !Objects.equals(card, "")) {
            return card;
        }
        return nick;
    }

    public Date getJoinTime() {
        return new Date(this.joinTime.getTime());
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = new Date(joinTime.getTime());
    }

    public Date getLastSpeakTime() {
        return new Date(this.lastSpeakTime.getTime());
    }

    public void setLastSpeakTime(Date lastSpeakTime) {
        this.lastSpeakTime = new Date(lastSpeakTime.getTime());
    }
}
