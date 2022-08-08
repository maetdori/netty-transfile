package com.netty.transfile.client;

import com.netty.transfile.client.codec.TransFileClientDecoder;
import com.netty.transfile.client.codec.TransFileClientEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;

@RequiredArgsConstructor
public class TransFileClientInitializer extends ChannelInitializer<SocketChannel> {
    private final Charset charset;

    @Override
    protected void initChannel(SocketChannel ch) {
       ch.pipeline()
               .addLast(new TransFileClientDecoder(charset))
               .addLast(new TransFileClientEncoder(charset))
               .addLast(new TransFileClientInboundHandler());
    }
}
