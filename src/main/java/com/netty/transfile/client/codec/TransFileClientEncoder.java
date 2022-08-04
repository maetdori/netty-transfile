package com.netty.transfile.client.codec;

import com.netty.transfile.common.dto.RequestDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@RequiredArgsConstructor
public class TransFileClientEncoder extends MessageToByteEncoder<RequestDataDto> {
    private final Charset charset;

    @Override
    protected void encode(ChannelHandlerContext ctx, RequestDataDto request, ByteBuf out) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(String.format("%08d", request.getLength()).getBytes(charset));
        outputStream.write(request.getOpcode().getBytes(charset));
        outputStream.write(request.getOpcode().getBytes(charset));
        outputStream.write(request.getData().getBytes(charset));
        outputStream.write(request.getFile());

        byte[] result = outputStream.toByteArray();

        out.writeBytes(result);
    }
}
