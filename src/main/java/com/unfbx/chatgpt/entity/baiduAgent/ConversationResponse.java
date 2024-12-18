package com.unfbx.chatgpt.entity.baiduAgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ConversationResponse {

    private String requestId;

    @JsonProperty("conversation_id")
    private String conversationId;
}
