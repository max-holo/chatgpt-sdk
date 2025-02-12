package com.unfbx.chatgpt.entity.weixinV2;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    DEEPSEEK_V3("deepseek-chat", "自动为v3模型"),
    DEEPSEEK_R1("deepseek-r1", "深度思考模型"),
    ERNIE_BOT_4("ernie-4.0-8k", "ernie-4.0-8k"),
    ;
    private final String code;
    private final String info;
}
