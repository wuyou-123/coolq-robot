package org.nico.ratel.landlords.client.handler;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.nico.ratel.landlords.entity.ClientTransferData.ClientTransferDataProtoc;

import java.util.List;

public class SecondProtobufCodec extends MessageToMessageCodec<ClientTransferDataProtoc, MessageLite> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLite msg, List<Object> out) throws Exception {
		out.add(msg);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ClientTransferDataProtoc msg, List<Object> out) throws Exception {
		out.add(msg);
	}

}
