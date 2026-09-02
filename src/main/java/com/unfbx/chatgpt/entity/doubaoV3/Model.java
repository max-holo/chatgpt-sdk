package com.unfbx.chatgpt.entity.doubaoV3;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    DOUBAO_1_6("doubao-seed-1-6-251015", "豆包多模态模型256k"),
    DOUBAO_2_1_PRO("doubao-seed-2-1-pro-260628", "豆包2.1多模态"),
    ;
    private final String code;
    private final String info;
}
