package com.netty.transfile.client.codec;

import com.netty.transfile.common.TransFileProtocol;
import com.netty.transfile.common.dto.ResponseDataDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;
import java.util.List;

@RequiredArgsConstructor
public class TransFileClientDecoder extends ByteToMessageDecoder {
    private final Charset charset;
    private int length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        ResponseDataDto responseDataDto = decode(in);
        if (responseDataDto != null) {
            out.add(responseDataDto);
        }
    }

    private ResponseDataDto decode(ByteBuf in) {
        if (in.readableBytes() > TransFileProtocol.HEADER_LENGTH && length == 0) {
            ByteBuf header = in.readBytes(TransFileProtocol.HEADER_LENGTH);
            this.length = Integer.parseInt(header.toString(charset));
        }

        if (in.readableBytes() == length) {
            String opcode = in.readBytes(TransFileProtocol.OPCODE_LENGTH).toString(charset);
            int data = Integer.parseInt(in.readBytes(length - TransFileProtocol.OPCODE_LENGTH).toString(charset));

            return ResponseDataDto.builder()
                    .opcode(opcode)
                    .data(data)
                    .build();
        }
        return null;
    }

}
