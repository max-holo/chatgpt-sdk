package com.unfbx.chatgpt.entity.deepseek;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    DEEPSEEK_V3("deepseek-chat", "自动为v3模型")
    ;
    private final String code;
    private final String info;
}
