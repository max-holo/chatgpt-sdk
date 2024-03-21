package com.unfbx.chatgpt.entity.weixin;

import lombok.Data;

import java.util.List;

@Data
public class BaiduCompletion {
    private List<Message> messages;
    private Boolean stream;
    private String user_id;
    private Double temperature;
    private Double top_p;
    private Double penalty_score;

    @Data
    public static class Message {
        private String role;
        private String content;

    }
}
