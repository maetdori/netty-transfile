package com.nettytransfile.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;

public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    private final int MAX_LENGTH = 65536;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new LineBasedFrameDecoder(MAX_LENGTH));
        pipeline.addLast(new StringDecoder());
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(new ClientHandler());
    }
}
