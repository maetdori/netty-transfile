package com.netty.transfile.server.codec;

import com.netty.transfile.common.TransFileProtocol;
import com.netty.transfile.common.dto.RequestDataDto;
import com.netty.transfile.common.enumeration.ResponseCode;
import com.netty.transfile.util.TransUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class TransFileServerDecoder extends ByteToMessageDecoder {

	private final Charset charset;

	public TransFileServerDecoder(Charset charset) {
		this.charset = charset;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		RequestDataDto data = decode(in);
		if (data != null) {
			out.add(data);
		}
	}

	private RequestDataDto decode(ByteBuf in) {
		int readableBytes = in.readableBytes();

		// check header
		if (readableBytes < TransFileProtocol.HEADER_LENGTH + TransFileProtocol.OPCODE_LENGTH) {
			return null;
		}

		// 1. read header length
		ByteBuf header = in.readBytes(TransFileProtocol.HEADER_LENGTH);

		// 2. convert dataLength and opcode
		int dataLength = Integer.parseInt(header.toString(charset)) - TransFileProtocol.OPCODE_LENGTH;
		String opcode = in.readBytes(TransFileProtocol.OPCODE_LENGTH).toString(charset);

		if (!TransFileProtocol.FILE_OPCODE.equals(opcode)) {
			return getRequestDataDto(ResponseCode.WRONG_FORMAT_ERROR);
		}

		// check data
		readableBytes = in.readableBytes();
		if (readableBytes < dataLength) {
			in.resetReaderIndex();
			return null;
		}

		// 3. make dto
		ByteBuf data = in.readBytes(dataLength);
		String rawData = data.toString(charset);
		RequestDataDto dataDto = TransUtil.convertRequestDataDto(rawData);
		if (dataDto == null) {
			in.discardReadBytes();
			return getRequestDataDto(ResponseCode.WRONG_FORMAT_ERROR);
		}

		// check file
		int fileSize = dataDto.getFileSize();
		if (fileSize < in.readableBytes()) {
			in.resetReaderIndex();
			return null;
		}

		// 4. set file
		ByteBuf file = in.readBytes(fileSize);
		dataDto.setFile(file.array());

		return dataDto;
	}

	private RequestDataDto getRequestDataDto(ResponseCode responseCode) {
		return RequestDataDto.builder()
				.responseCode(responseCode)
				.build();
	}
}
