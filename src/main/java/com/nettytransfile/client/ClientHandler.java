package com.nettytransfile.client;

import com.nettytransfile.define.ResponseCode;
import com.nettytransfile.define.ResponseCodeDetail;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.ByteBuffer;

public class ClientHandler extends SimpleChannelInboundHandler {
    private ByteBuf buffer;
    private int length = 0;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buffer = ctx.alloc().buffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        clearBuffer();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        buffer.writeBytes((ByteBuf) msg);

        int readableByte = buffer.readableBytes();

        if (readableByte > 8 && length == 0) {
            byte[] bytes = new byte[8];
            buffer.getBytes(0, bytes);
            this.length = ByteBuffer.wrap(bytes).getInt();
        }

        if (readableByte == length) {
            byte[] bytes = new byte[buffer.readableBytes()];
            buffer.readBytes(bytes);
            Packet packet = new Packet(new String(bytes));
            packet.readData();

            clearBuffer();
        }
    }

    private void clearBuffer() {
        if (buffer != null) {
            buffer.release();
            buffer = null;
        }
    }

    class Packet {
        private String opcode;
        private String data;

        public Packet(String message) {
            this.opcode = message.substring(0, 2);
            this.data = message.substring(2);
        }

        public void readData() {
            String responseType = ResponseCode.valueOf(opcode).getMessage();
            String responseMessage = ResponseCodeDetail.getMessageOf(Integer.parseInt(data));

            System.out.println("responseType: " + responseType);
            System.out.println("responseMessage: " + responseMessage);
        }
    }
}
