package com.nettytransfile.define;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    OK("성공"), NO("실패");

    private final String message;
}
