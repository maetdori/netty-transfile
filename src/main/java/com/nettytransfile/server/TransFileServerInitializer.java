package com.nettytransfile.server;

import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.nio.charset.Charset;

public class TransFileServerInitializer extends ChannelInitializer<SocketChannel> {

	private final Charset charset;
	private final int READ_TIMEOUT = 60;

	public TransFileServerInitializer(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.config().setAllocator(UnpooledByteBufAllocator.DEFAULT);
		ch.pipeline()
				.addLast(new ReadTimeoutHandler(READ_TIMEOUT))
				.addLast(new TransFileServerDecoder(charset))
				.addLast(new TransFileServerEncoder(charset))
				.addLast(new TransFileServerInboundHandler());
	}
}
