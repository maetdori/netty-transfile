package com.nettytransfile.client;

import com.nettytransfile.define.RequestCmd;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class Client {
    private static final int SERVER_PORT = 12345;
    private static final String OPCODE = "FI";
    private static final String FILE_PATH = "/Users/maetdori/Desktop/miao.jpeg";
    private static final String FILE_NAME = "miao.jpeg";
    private static final String FILE_DIR = "/home/service/file/";
    private static final int FILE_SIZE = 29401;

    private final String host;
    private final int port;

    private Channel serverChannel;
    private EventLoopGroup eventLoopGroup;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws InterruptedException {
        eventLoopGroup = new NioEventLoopGroup(1);

        Bootstrap bootstrap = new Bootstrap().group(eventLoopGroup);

        bootstrap.channel(NioSocketChannel.class);
        bootstrap.remoteAddress(new InetSocketAddress(host, port));
        bootstrap.handler(new ClientInitializer());

        serverChannel = bootstrap.connect().sync().channel();
    }

    public void send(String cmd) throws InterruptedException, IOException {
        ChannelFuture future;
        String delimiter = "::=";
        String crlf = "\r\n";

        String data = "CMD" + delimiter + cmd + crlf +
                "FILENAME" + delimiter + FILE_NAME + crlf +
                "FILESIZE" + delimiter + FILE_SIZE + crlf +
                "DIR" + delimiter + FILE_DIR + crlf;

        String length = String.format("%08d", data.length() + OPCODE.length());
        File file = new File(FILE_PATH);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(length.getBytes(StandardCharsets.UTF_8));
        outputStream.write(OPCODE.getBytes(StandardCharsets.UTF_8));
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        outputStream.write(Files.readAllBytes(file.toPath()));

        byte[] request = outputStream.toByteArray();

        future = serverChannel.writeAndFlush(request);

        if (future != null) {
            future.sync();
        }
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        Client client = new Client("127.0.0.1", SERVER_PORT);

        try {
            client.connect();
            client.send(RequestCmd.CREATE.getCode());
        } finally {
            client.close();
        }
    }
}
