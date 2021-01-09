package org.nico.ratel.landlords.utils;

import com.wuyou.utils.GlobalVariable;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.server.ServerContains;

public class GetQQUtils {
    public static String getQQ(Channel channel) {
        String longId = channel.localAddress().toString();
        //        System.out.println(longId+"========"+clientId);
//        if (null == clientId) {
////            clientId = ServerContains.getClientId();
//            ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
//        }
//        return GlobalVariable.CLIENT_ID_MAP.get(clientId);
        return ServerContains.CHANNEL_ID_MAP.get(longId);
    }
    public static String getGroup(Channel channel) {
        String longId = channel.localAddress().toString();
        String clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
//        if (null == clientId) {
//            clientId = ServerContains.getClientId();
//            ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
//        }
        return GlobalVariable.CLIENT_ID_MAP.get(clientId);
    }
    public static ClientSide getClient(Channel channel){
        String longId = channel.localAddress().toString();
        String clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
        return ServerContains.CLIENT_SIDE_MAP.get(clientId);
    }
}
