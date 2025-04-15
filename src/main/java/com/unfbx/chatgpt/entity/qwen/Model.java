package com.unfbx.chatgpt.entity.qwen;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    QWEN_MAX("qwen-max", "32k"),
    QWEN_TURBO("qwen-turbo", "128k"),
    QWEN_PLUS("qwen-plus", "32k"),
    ;
    private final String code;
    private final String info;
}
