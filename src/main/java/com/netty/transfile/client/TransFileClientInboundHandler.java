package com.netty.transfile.client;

import com.netty.transfile.common.dto.ResponseDataDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class TransFileClientInboundHandler extends SimpleChannelInboundHandler<ResponseDataDto> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ResponseDataDto responseDataDto) {
        responseDataDto.readData();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
