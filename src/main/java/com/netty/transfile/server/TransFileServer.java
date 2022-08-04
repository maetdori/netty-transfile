package com.netty.transfile.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.Charset;

@Slf4j
public class TransFileServer {

	@Value("${trans.server.boss.count: 1}")
	private int bossThreadCnt;

	@Value("${trans.server.worker.count: 8}")
	private int workerThreadCnt;

	@Value("${trans.server.port}")
	private int port;

	private final Charset charset;

	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ServerBootstrap serverBootstrap;

	public TransFileServer(Charset charset) {
		if (charset == null) {
			this.charset = CharsetUtil.UTF_8;
		} else {
			this.charset = charset;
		}
	}

	public void serverInit() {
		bossGroup = new NioEventLoopGroup(bossThreadCnt);
		workerGroup = new NioEventLoopGroup(workerThreadCnt);

		serverBootstrap = new ServerBootstrap()
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new TransFileServerInitializer(charset))
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.SO_BACKLOG, 50);
	}

	public void start() {
		try {
			ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			log.error(e.getClass().getSimpleName() + " : {}", e.getMessage(), e);
		}
	}

	public void serverDestroy() {
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}
}
