package org.nico.ratel.landlords.utils;

import com.wuyou.utils.GlobalVariable;
import io.netty.channel.Channel;
import org.nico.ratel.landlords.server.ServerContains;

public class GetQQUtils {
    public static String getQQ(Channel channel) {
        String longId = channel.id().asLongText();
        Integer clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
        if (null == clientId) {
            clientId = ServerContains.getClientId();
            ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
        }
        return GlobalVariable.CLIENT_ID_MAP.get(clientId);
    }
}
