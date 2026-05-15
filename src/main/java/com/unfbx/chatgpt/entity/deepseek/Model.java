package com.unfbx.chatgpt.entity.deepseek;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    DEEPSEEK_V4_PRO("deepseek-v4-pro", "v4-pro")
    ;
    private final String code;
    private final String info;
}
