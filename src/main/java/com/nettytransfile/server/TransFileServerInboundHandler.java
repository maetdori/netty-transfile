package com.nettytransfile.server;

import com.nettytransfile.model.RequestDataDto;
import com.nettytransfile.model.ResponseDataDto;
import com.nettytransfile.model.TransFileProtocol;
import com.nettytransfile.model.enumeration.ResponseCode;
import com.nettytransfile.util.TransUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransFileServerInboundHandler extends SimpleChannelInboundHandler<RequestDataDto> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RequestDataDto dataDto) throws Exception {

		ResponseCode responseCode = dataDto.getResponseCode();

		if (dataDto.getResponseCode() == null) {
			switch (dataDto.getCmd()) {
				case TransFileProtocol.CMD_CREATE :
					responseCode = TransUtil.saveFile(dataDto) ? ResponseCode.SUCCESS : ResponseCode.FILE_CREATE_FAILED;
					break;
				case TransFileProtocol.CMD_DELETE:
					responseCode = TransUtil.removeFile(dataDto) ? ResponseCode.SUCCESS : ResponseCode.FILE_DELETE_FAILED;
					break;
				default:
					responseCode = ResponseCode.WRONG_CMD;
					break;
			}
		}

		ResponseDataDto responseDataDto = ResponseDataDto.builder()
				.opcode(responseCode == ResponseCode.SUCCESS ? TransFileProtocol.SUCCESS_OPCODE : TransFileProtocol.FAILED_OPCODE)
				.data(responseCode.getCode())
				.build();

		ctx.writeAndFlush(responseDataDto);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error(cause.getClass().getSimpleName() + " : {}", cause.getMessage(), cause);

		ResponseDataDto responseDataDto = ResponseDataDto.builder()
				.opcode(TransFileProtocol.FAILED_OPCODE)
				.data(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
				.build();

		ctx.writeAndFlush(responseDataDto);
	}
}
