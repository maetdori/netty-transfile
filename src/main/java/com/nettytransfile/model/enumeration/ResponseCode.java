package com.nettytransfile.model.enumeration;

import lombok.Getter;

@Getter
public enum ResponseCode {

	SUCCESS(8000, "성공"),

	WRONG_FORMAT_ERROR(8001, "전문 형식 에러"),
	WRONG_CMD(8002, "잘못된 CMD"),
	FILE_CREATE_FAILED(8003, "파일 생성 실패"),
	FILE_MODIFIED_FAILED(8004, "파일 변경 실패"),
	FILE_DELETE_FAILED(8005, "파일 삭제 실패"),

	INTERNAL_SERVER_ERROR(9999, "서버 내부 에러"),
	;

	private final int code;
	private final String message;

	ResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
