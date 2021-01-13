package com.wuyou.entity;

import com.wuyou.enums.ClientRole;
import com.wuyou.enums.ClientStatus;
import com.wuyou.enums.ClientType;
import lombok.Getter;
import lombok.Setter;
import org.nico.ratel.landlords.entity.Poker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wuyou
 */
@Getter
@Setter
public class Player {

    private String id;

    private String roomId;

    private String nickname;

    private List<Poker> pokers;

    private ClientRole role;

    private ClientStatus status;

    private ClientType type;

    private Player next;

    private Player pre;

    private Map<String, String> data;

    public String get(String key){
        if(data==null){
            data = new HashMap<>();
        }
        return data.get(key);
    }

    public String put(String key, String value){
        return data.put(key, value);
    }

    public Player() {
    }

    public Player(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.role = ClientRole.PLAYER;
    }

    public void init() {
        roomId = "0";
        pokers = null;
        status = ClientStatus.TO_CHOOSE;
        type = null;
        next = null;
        pre = null;
        data = new HashMap<>();
    }
}
