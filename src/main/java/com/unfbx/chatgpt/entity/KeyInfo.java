package com.unfbx.chatgpt.entity;

import lombok.Data;

@Data
public class KeyInfo {
    private String key;
    private String apiHost;

    public KeyInfo(String key) {
        this.key = key;
    }

    public KeyInfo(String key, String apiHost) {
        this.key = key;
        this.apiHost = apiHost;
    }

    public KeyInfo() {
    }

}