package com.nettytransfile.server;

import com.nettytransfile.model.RequestDataDto;
import com.nettytransfile.model.TransFileProtocol;
import com.nettytransfile.model.enumeration.ResponseCode;
import com.nettytransfile.util.TransUtil;
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

		// check data
		readableBytes = in.readableBytes();
		if (readableBytes < dataLength) {
			in.resetReaderIndex();
			return null;
		}

		ByteBuf data = in.readBytes(dataLength);
		String rawData = data.toString(charset);

		// 3. make dto
		RequestDataDto dataDto = TransUtil.convertRequestDataDto(rawData);
		if (dataDto == null) {
			return RequestDataDto.builder()
					.responseCode(ResponseCode.WRONG_FORMAT_ERROR)
					.build();
		}

		int fileSize = dataDto.getFileSize();
		if (fileSize < in.readableBytes()) {
			in.resetReaderIndex();
			return null;
		}

		ByteBuf byteBuf = in.readBytes(fileSize);
		byte[] array = byteBuf.array();
		dataDto.setFile(array);

		return dataDto;
	}
}
