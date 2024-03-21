package com.unfbx.chatgpt.function;

import cn.hutool.core.util.RandomUtil;
import com.unfbx.chatgpt.entity.KeyInfo;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;

import java.util.List;


public class KeyRandomStrategy implements KeyStrategy {
    public KeyRandomStrategy() {
    }

    public KeyInfo strategy(ChatCompletion.ModelType modelType) {
        return (KeyInfo)RandomUtil.randomEle(this.getKeys(modelType));
    }

    public List<KeyInfo> getKeys(ChatCompletion.ModelType modelType) {
        return LOCAL_KEYS;
    }
}
