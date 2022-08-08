package com.netty.transfile.common.dto;

import com.google.gson.annotations.SerializedName;
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

	@SerializedName(value = "OPCODE", alternate = {"opcode", "opCode"})
	private String opcode;

	@SerializedName(value = "DATA", alternate = "data")
	private String data;

	@SerializedName(value = "CMD", alternate = "cmd")
	private char cmd;

	@SerializedName(value = "FILENAME", alternate = {"filename", "fileName"})
	private String fileName;

	@SerializedName(value = "FILESIZE", alternate = {"filesize", "fileSize"})
	private int fileSize;

	@SerializedName(value = "DIR", alternate = "dir")
	private String dir;

	@SerializedName(value = "FILE", alternate = "file")
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
