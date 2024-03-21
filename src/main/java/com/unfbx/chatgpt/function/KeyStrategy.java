package com.unfbx.chatgpt.function;

import com.unfbx.chatgpt.entity.KeyInfo;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;

import java.util.ArrayList;
import java.util.List;

public interface KeyStrategy {
    List<KeyInfo> LOCAL_KEYS = new ArrayList();

    KeyInfo strategy(ChatCompletion.ModelType var1);

    List<KeyInfo> getKeys(ChatCompletion.ModelType var1);

    default KeyStrategy initKey(List<KeyInfo> keys) {
        LOCAL_KEYS.addAll(keys);
        return this;
    }

    default void removeErrorKey(String key, String desc) {
    }

    default void keysWarring() {
    }
}
