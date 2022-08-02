package com.nettytransfile.define;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeDetail {
    SUCCESS(8000, "성공"),
    ERR_FORMAT(8001, "전문 형식 에러"),
    ERR_CMD(8002, "잘못된 CMD"),
    ERR_CREATE(8003, "파일 생성 실패"),
    ERR_MODIFY(8004, "파일 변경 실패"),
    ERR_DELETE(8005, "파일 삭제 실패"),
    ERR_INTERNAL(9999, "내부 서버 에러");

    private final int code;
    private final String message;

    public static String getMessageOf(int code) {
        for (ResponseCodeDetail element: ResponseCodeDetail.values()) {
            if (element.getCode() == code) {
                return element.getMessage();
            }
        }
        return null;
    }
}
