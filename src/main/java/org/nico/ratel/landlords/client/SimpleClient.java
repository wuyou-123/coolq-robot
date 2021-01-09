package org.nico.ratel.landlords.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.nico.ratel.landlords.client.handler.DefaultChannelInitializer;
import org.nico.ratel.landlords.server.ServerContains;

public class SimpleClient {

    public static String id = "-1";

    public static String serverAddress = "127.0.0.1";

    public static int port = 1028;

    public static void main(String[] args) {
        EventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        try {

//            if (GlobalVariable.LANDLORDS_PLAYER.get(group) == null) {
//            GlobalVariable.LANDLORDS_PLAYER.put(group, new HashMap<>());
            Bootstrap bootstrap = new Bootstrap()
                    .group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new DefaultChannelInitializer());
            Channel channel = bootstrap.connect("127.0.0.1", ServerContains.port).sync().channel();
//            GlobalVariable.LANDLORDS_PLAYER.get(group).put(qq, channel);
//            pushToServer(channel, ServerEventCode.CODE_CLIENT_NICKNAME_SET, "1097810498");
//            System.out.println(111);
//                //get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, "1");
//            pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE, null);
//            pushToServer(channel, ServerEventCode.CODE_GET_ROOMS, null);

//            ClientEventListener.//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, "2");
//            ClientEventListener.//get(ClientEventCode.CODE_SHOW_OPTIONS).call(channel, "2");
//                get(ClientEventCode.CODE_SHOW_ROOMS).call(channel, "");

//                channelFuture.sync();
//                channelFuture.addListener()
//            } else {
//                Bootstrap bootstrap = new Bootstrap()
//                        .group(nioEventLoopGroup)
//                        .channel(NioSocketChannel.class)
//                        .handler(new DefaultChannelInitializer());
//                Channel channel = bootstrap.connect("127.0.0.1", ServerContains.port).sync().channel();
//                GlobalVariable.LANDLORDS_PLAYER.get(group).put(qq, channel);
//                pushToServer(channel, ServerEventCode.CODE_CLIENT_NICKNAME_SET, msg.getAccountInfo().getAccountRemarkOrNickname());
////                pushToServer(channel, ServerEventCode.CODE_ROOM_CREATE, null);
////                get(ClientEventCode.CODE_SHOW_ROOMS).call(channel, "");
//
//            }
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }

}
