package com.netty.transfile.server.codec;

import com.netty.transfile.common.TransFileProtocol;
import com.netty.transfile.common.dto.RequestDataDto;
import com.netty.transfile.common.enumeration.ResponseCode;
import com.netty.transfile.util.TransUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
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

		RequestDataDto dataDto = null;
		ByteBuf header = null, data = null, file = null;
		try {
			int readableBytes = in.readableBytes();

			// check header
			if (readableBytes < TransFileProtocol.HEADER_LENGTH + TransFileProtocol.OPCODE_LENGTH) {
				return null;
			}

			// 1. read header length
			header = in.readBytes(TransFileProtocol.HEADER_LENGTH);

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
			data = in.readBytes(dataLength);
			String rawData = data.toString(charset);
			dataDto = TransUtil.convertRequestDataDto(rawData);
			if (dataDto == null) {
				in.discardReadBytes();
				return getRequestDataDto(ResponseCode.WRONG_FORMAT_ERROR);
			}

			// check file
			int fileSize = dataDto.getFileSize();
			readableBytes = in.readableBytes();
			if (readableBytes < fileSize) {
				in.resetReaderIndex();
				return null;
			}

			// 4. set file
			file = in.readBytes(fileSize);
			dataDto.setFile(new byte[fileSize]);

			byte[] array;
			if (file.hasArray()) {
				array = file.array();
			} else {
				array = new byte[file.readableBytes()];
			}
			file.getBytes(file.readerIndex(), array);
			dataDto.setFile(array);

		} catch (Exception e) {
			log.error(e.getClass().getSimpleName() + " : {}", e.getMessage(), e);
		} finally {
			ReferenceCountUtil.release(header);
			ReferenceCountUtil.release(data);
			ReferenceCountUtil.release(file);
		}

		return dataDto;
	}

	private RequestDataDto getRequestDataDto(ResponseCode responseCode) {
		return RequestDataDto.builder()
				.responseCode(responseCode)
				.build();
	}
}
