package com.netty.transfile.common;

public class TransFileProtocol {

	public static final int HEADER_LENGTH = 8;
	public static final int OPCODE_LENGTH = 2;

	public static final String CRLF = "\r\n";
	public static final String DELIMITER = "::=";

	public static final String SUCCESS_OPCODE = "OK";
	public static final String FAILED_OPCODE = "NO";
	public static final String FILE_OPCODE = "FI";

	public static final char CMD_CREATE = 'C';
	public static final char CMD_DELETE = 'D';
}
