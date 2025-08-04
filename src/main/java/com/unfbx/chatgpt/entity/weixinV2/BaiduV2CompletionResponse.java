package com.unfbx.chatgpt.entity.weixinV2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaiduV2CompletionResponse implements Serializable {

    private String id;
    private String object;
    private Long created;
    private String model;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
    private List<Choice> choices;
    private Usage usage;
    private SearchResults searchResults;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Long index;
        @JsonProperty("finish_reason")
        private String finishReason;
        private Boolean flag;
        private Boolean ban_round;
        private Messages message;
        private Messages delta;
    }

    @Data
    public static class Messages {
        private static final ObjectMapper objectMapper = new ObjectMapper();
        private String role;
        private String content;
        @JsonProperty("reasoning_content")
        private String reasoningContent;

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
        private PromptTokensDetails prompt_tokens_details;
        private CompletionTokensDetails completion_tokens_details;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchResults {
        private int index;
        private String url;
        private String title;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromptTokensDetails {
        private int search_tokens;
        private int cached_tokens;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletionTokensDetails {
        private int reasoning_tokens;
    }
}
