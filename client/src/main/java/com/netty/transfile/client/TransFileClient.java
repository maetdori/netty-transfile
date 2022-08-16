package com.netty.transfile.client;

import com.netty.transfile.common.TransFileProtocol;
import com.netty.transfile.common.dto.RequestDataDto;
import com.netty.transfile.common.dto.ResponseDataDto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class TransFileClient {
    @Value("${trans.client.file.path}")
    private String filePath;

    @Value("${trans.client.file.name}")
    private String fileName;

    @Value("${trans.client.file.dest}")
    private String fileDest;

    @Value("${trans.server.port}")
    private int port;

    private final Charset charset;

    private EventLoopGroup eventLoopGroup;
    private Bootstrap bootstrap;

    @Getter
    @Setter
    private ResponseDataDto responseDataDto;

    public TransFileClient(Charset charset) {
        if (charset == null) {
            this.charset = CharsetUtil.UTF_8;
        } else {
            this.charset = charset;
        }
    }

    public void connect(ChannelFutureListener listener) throws InterruptedException {
        eventLoopGroup = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();

        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress(new InetSocketAddress(port))
                .handler(new TransFileClientInitializer(this, charset))
                .connect()
                .sync()
                .addListener(listener)
                .channel()
                .closeFuture()
                .sync();
    }

    public void send(char cmd) throws IOException, InterruptedException {
        File file = new File(filePath + fileName);

        RequestDataDto requestDataDto = RequestDataDto.builder()
                .opcode(TransFileProtocol.FILE_OPCODE)
                .cmd(cmd)
                .fileName(fileName)
                .fileSize((int) Files.size(file.toPath()))
                .dir(fileDest)
                .file(Files.readAllBytes(file.toPath()))
                .build();

        requestDataDto.createData();

        connect(future -> {
            try {
                if (!future.isSuccess()) {
                    throw new IllegalStateException("operation failed.");
                }
                if (!future.channel().isActive() || !future.channel().isWritable()) {
                    throw new IllegalStateException("channel is inActive.");
                }
                future.channel()
                        .writeAndFlush(requestDataDto)
                        .addListener((ChannelFutureListener) cf -> {
                            if (!cf.isSuccess()) {
                                close();
                            }
                        });
            } catch (Exception e) {
                close();
            }
        });
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
