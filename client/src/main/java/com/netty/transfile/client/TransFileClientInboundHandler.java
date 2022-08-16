package com.netty.transfile.client;

import com.netty.transfile.common.dto.ResponseDataDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TransFileClientInboundHandler extends SimpleChannelInboundHandler<ResponseDataDto> {

    private final TransFileClient transFileClient;

    public TransFileClientInboundHandler(TransFileClient transFileClient) {
        this.transFileClient = transFileClient;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseDataDto responseDataDto) {
        responseDataDto.readData();
        transFileClient.setResponseDataDto(responseDataDto);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }
}
