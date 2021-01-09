package org.nico.ratel.landlords.server.handler;

import com.wuyou.utils.GlobalVariable;
import com.wuyou.utils.landlordsPrint.SimplePrinter;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.ServerTransferData.ServerTransferDataProtoc;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.server.ServerContains;
import org.nico.ratel.landlords.server.event.ServerEventListener;

public class TransferHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        Channel ch = ctx.channel();
//        System.out.println(ctx.channel().hasAttr(ATTRIBUTE_KEY));
//        System.out.println(ctx);

        //init client info

//        ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_NICKNAME_SET, null);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ServerTransferDataProtoc) {
            ServerTransferDataProtoc serverTransferData = (ServerTransferDataProtoc) msg;
            ServerEventCode code = ServerEventCode.valueOf(serverTransferData.getCode());
            if (code != ServerEventCode.CODE_CLIENT_HEAD_BEAT) {
                ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(getId(ctx.channel()));
//                if (client == null) {
//                    client = ServerContains.CLIENT_SIDE_MAP.get(getId(ctx.channel()));
//                    System.out.println(client);
//                }
                if (GlobalVariable.CLIENT_ID_MAP.get(client.getId()) != null) {
                    SimplePrinter.serverLog(GlobalVariable.CLIENT_ID_MAP.get(client.getId()) + " | " + client.getNickname() + " do:" + code.getMsg());
                } else {
                    SimplePrinter.serverLog(client.getId() + " | " + client.getNickname() + " do:" + code.getMsg());
                }
                ServerEventListener.get(code).call(client, serverTransferData.getData());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof java.io.IOException) {
            cause.printStackTrace();
            System.out.println("-----------------------------------------\n" + cause.getMessage());
            clientOfflineEvent(ctx.channel());
        } else {
            SimplePrinter.serverLog("ERROR：" + cause.getMessage());
            cause.printStackTrace();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                try {
                    clientOfflineEvent(ctx.channel());
                    ctx.channel().close();
                } catch (Exception ignored) {
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private String getId(Channel channel) {
        String longId = channel.remoteAddress().toString();
//        System.out.println(channel.hasAttr(ATTRIBUTE_KEY));
//        if (channel.hasAttr(ATTRIBUTE_KEY)) {
//        System.out.println(channel.attr(ATTRIBUTE_KEY));
//        System.out.println("aaaa" + channel.attr(ATTRIBUTE_KEY).get());
//            longId = channel.attr(ATTRIBUTE_KEY).get();
//        }
        //        if (null == clientId) {
////            clientId = ServerContains.getClientId();
//            ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
//        }
        return ServerContains.CHANNEL_ID_MAP.get(longId);
    }

    private void clientOfflineEvent(Channel channel) {
        String clientId = getId(channel);
        ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(GlobalVariable.CLIENT_ID_MAP.get(clientId));
        if (client != null) {
            SimplePrinter.serverLog("Has client exit to the server：" + clientId + " | " + client.getNickname());
            ServerEventListener.get(ServerEventCode.CODE_CLIENT_OFFLINE).call(client, null);
            GlobalVariable.LANDLORDS_PLAYER.remove(client.getNickname());
        }
    }
}
