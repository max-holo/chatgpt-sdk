package com.unfbx.chatgpt.entity.chatglm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatglmCompletionResponse implements Serializable {

    private String id;
    @JsonProperty("request_id")
    private String requestId;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Long index;
        @JsonProperty("finish_reason")
        private String finishReason;
        private Messages message;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Messages {
        private String role;
        private String content;
        @JsonProperty("reasoning_content")
        private String reasoningContent;
        private static final ObjectMapper objectMapper = new ObjectMapper();
        @Override
        public String toString() {
            try {
                return objectMapper.writeValueAsString(this);
            } catch (JsonProcessingException e) {
                // 序列化失败时返回简要错误信息
                return String.format("{\"error\":\"Failed to serialize Messages: %s\"}", e.getMessage());
            }
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }

}
