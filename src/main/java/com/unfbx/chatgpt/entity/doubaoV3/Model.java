package com.unfbx.chatgpt.entity.doubaoV3;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    DOUBAO_1_6("doubao-seed-1-6-250615", "豆包多模态模型"),
    ;
    private final String code;
    private final String info;
}
