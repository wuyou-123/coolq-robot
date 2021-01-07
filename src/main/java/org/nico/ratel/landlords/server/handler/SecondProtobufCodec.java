package org.nico.ratel.landlords.server.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.nico.ratel.landlords.entity.ServerTransferData.ServerTransferDataProtoc;

import java.util.List;

public class SecondProtobufCodec extends MessageToMessageCodec<ServerTransferDataProtoc, MessageLite> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLite msg, List<Object> out) throws Exception {
		out.add(msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ServerTransferDataProtoc msg, List<Object> out) throws Exception {
		out.add(msg);
	}


}
