package com.unfbx.chatgpt.entity.qwen;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    QWEN_MAX("qwen-max", "8k"),
    QWEN_TURBO("qwen-turbo", "8k"),
    QWEN_PLUS("qwen-plus", "32k"),
    ;
    private final String code;
    private final String info;
}
