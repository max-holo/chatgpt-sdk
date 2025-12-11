package com.unfbx.chatgpt.entity.weixinV2;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    DEEPSEEK_V3("deepseek-v3.2-think", "自动为v3模型"),
    DEEPSEEK_R1("deepseek-r1", "深度思考模型"),
    ERNIE_BOT_4("ernie-4.0-8k", "ernie-4.0-8k模型"),
    ERNIE_BOT_3_5("ernie-3.5-128k", "3.5-128k模型"),
    ERNIE_BOT_4_5("ernie-4.5-turbo-32k", "4.5-32k模型"),
    EMBEDDING_V1("embedding-v1", "embedding-v1模型"),
    ;
    private final String code;
    private final String info;
}
