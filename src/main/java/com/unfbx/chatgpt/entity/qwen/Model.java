package com.unfbx.chatgpt.entity.qwen;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    QWEN_MAX("qwen3.7-max", ""),
    QWEN_PLUS("qwen3.7-plus", ""),
    ;
    private final String code;
    private final String info;
}
