package com.unfbx.chatgpt.entity.baiduAgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;

@Data
public class AppBuilderClientResponse implements Serializable {
    @JsonProperty("request_id")
    private String requestId;
    private String date;
    private String answer;
    @JsonProperty("conversation_id")
    private String conversationId;
    @JsonProperty("message_id")
    private String messageId;
    @JsonProperty("is_completion")
    private boolean isCompletion;
    private EventContent[] content;


    @Override
    public String toString() {
        return "AgentBuilderResponse{" +
                "requestId='" + requestId + '\'' +
                ", date='" + date + '\'' +
                ", answer='" + answer + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", isCompletion=" + isCompletion +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
