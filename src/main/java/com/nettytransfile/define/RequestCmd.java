package com.nettytransfile.define;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestCmd {
    CREATE("C"), MODIFY("M"), DELETE("D");

    private final String code;
}
