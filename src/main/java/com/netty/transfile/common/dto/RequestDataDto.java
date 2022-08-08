package com.netty.transfile.common.dto;

import com.netty.transfile.common.TransFileProtocol;
import com.netty.transfile.common.enumeration.ResponseCode;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDataDto {
	private String opcode;
	private String data;
	private char cmd;
	private String fileName;
	private int fileSize;
	private String dir;
	private byte[] file;

	private ResponseCode responseCode;

	public int getLength() {
		return TransFileProtocol.OPCODE_LENGTH + this.data.length();
	}

	public void createData() {
		this.data = "CMD" + TransFileProtocol.DELIMITER + this.cmd + TransFileProtocol.CRLF +
				"FILENAME" + TransFileProtocol.DELIMITER + this.fileName + TransFileProtocol.CRLF +
				"FILESIZE" + TransFileProtocol.DELIMITER + this.fileSize + TransFileProtocol.CRLF +
				"DIR" + TransFileProtocol.DELIMITER + this.dir + TransFileProtocol.CRLF;
	}
}
