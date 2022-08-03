package com.nettytransfile.server;

import com.nettytransfile.model.ResponseDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.Charset;

@Slf4j
public class TransFileServerEncoder extends MessageToByteEncoder<ResponseDataDto> {

	private final Charset charset;
	private String remoteAddress;

	public TransFileServerEncoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, ResponseDataDto response, ByteBuf out) throws Exception {
		try {
			if (response == null) {
				throw new NullPointerException("response is null");
			}

			String resultBody = response.getOpcode() + response.getData();
			String result = String.format("%08d", resultBody.getBytes(charset).length) + resultBody;

			out.writeBytes(Unpooled.wrappedBuffer(result.getBytes(charset)));
		} catch (NullPointerException e) {
			log.error(e.getClass().getSimpleName() + " : {}, remoteAddress={}", e.getMessage(), remoteAddress, e);
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		SocketAddress socketAddress = ctx.channel().remoteAddress();
		if (socketAddress instanceof InetSocketAddress) {
			InetSocketAddress remote = (InetSocketAddress) socketAddress;
			remoteAddress = remote.getAddress() + ":" +remote.getPort();
		}
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		remoteAddress = null;
	}
}
