package com.unfbx.chatgpt.entity.moonshot;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Model {

    MOONSHOT_V1_8K("moonshot-v1-8k", "8k"),
    MOONSHOT_V1_32K("moonshot-v1-32k","32k"),
    MOONSHOT_V1_128K("moonshot-v1-128k","128k"),
    ;
    private final String code;
    private final String info;
}
