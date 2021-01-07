package org.nico.ratel.landlords.server.handler;

import com.wuyou.utils.GlobalVariable;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.nico.ratel.landlords.client.event.ClientEventListener;
import org.nico.ratel.landlords.entity.ClientSide;
import org.nico.ratel.landlords.entity.ServerTransferData.ServerTransferDataProtoc;
import org.nico.ratel.landlords.enums.ClientEventCode;
import org.nico.ratel.landlords.enums.ClientRole;
import org.nico.ratel.landlords.enums.ClientStatus;
import org.nico.ratel.landlords.enums.ServerEventCode;
import org.nico.ratel.landlords.print.SimplePrinter;
import org.nico.ratel.landlords.server.ServerContains;
import org.nico.ratel.landlords.server.event.ServerEventListener;

public class TransferHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Channel ch = ctx.channel();
//        System.out.println(ctx.channel().hasAttr(ATTRIBUTE_KEY));
//        System.out.println(ctx);

        //init client info
        ClientSide clientSide = new ClientSide(getId(ctx.channel()), ClientStatus.TO_CHOOSE, ch);
        clientSide.setNickname(String.valueOf(clientSide.getId()));
        clientSide.setRole(ClientRole.PLAYER);
        ServerContains.CLIENT_SIDE_MAP.put(clientSide.getId() + "", clientSide);

        SimplePrinter.serverLog("Has client connect to the server：" + clientSide.getId());
        System.out.println("客户端id: " + clientSide.getId());
//        ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_CONNECT, String.valueOf(clientSide.getId()));
        ClientEventListener.get(ClientEventCode.CODE_CLIENT_CONNECT).call(ch, String.valueOf(clientSide.getId()));

//        ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_NICKNAME_SET, null);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ServerTransferDataProtoc) {
            ServerTransferDataProtoc serverTransferData = (ServerTransferDataProtoc) msg;
            ServerEventCode code = ServerEventCode.valueOf(serverTransferData.getCode());
            if (code != null && code != ServerEventCode.CODE_CLIENT_HEAD_BEAT) {
                ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(String.valueOf(getId(ctx.channel())));
                if (client == null) {
                    client = ServerContains.CLIENT_SIDE_MAP.get(GlobalVariable.CLIENT_ID_MAP.get(getId(ctx.channel())));
                }
                if (GlobalVariable.CLIENT_ID_MAP.get(client.getId()) != null)
                    SimplePrinter.serverLog(GlobalVariable.CLIENT_ID_MAP.get(client.getId()) + " | " + client.getNickname() + " do:" + code.getMsg());
                else
                    SimplePrinter.serverLog(client.getId() + " | " + client.getNickname() + " do:" + code.getMsg());
                ServerEventListener.get(code).call(client, serverTransferData.getData());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        if (cause instanceof java.io.IOException) {
            cause.printStackTrace();
            System.out.println("-----------------------------------------\n"+cause.getMessage());
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
                } catch (Exception e) {
                }
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    private int getId(Channel channel) {
        String longId = channel.id().asLongText();
//        System.out.println(channel.hasAttr(ATTRIBUTE_KEY));
//        if (channel.hasAttr(ATTRIBUTE_KEY)) {
//        System.out.println(channel.attr(ATTRIBUTE_KEY));
//        System.out.println("aaaa" + channel.attr(ATTRIBUTE_KEY).get());
//            longId = channel.attr(ATTRIBUTE_KEY).get();
//        }
        Integer clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
        if (null == clientId) {
            clientId = ServerContains.getClientId();
            ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
        }
        return clientId;
    }

    private void clientOfflineEvent(Channel channel) {
        int clientId = getId(channel);
        ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(GlobalVariable.CLIENT_ID_MAP.get(clientId));
        if (client != null) {
            SimplePrinter.serverLog("Has client exit to the server：" + clientId + " | " + client.getNickname());
            ServerEventListener.get(ServerEventCode.CODE_CLIENT_OFFLINE).call(client, null);
        }
        GlobalVariable.LANDLORDS_PLAYER.forEach((k,v)->{
            assert client != null;
            if(v.get(client.getNickname())!=null){
                v.remove(client.getNickname());
            }
        });
    }
}
