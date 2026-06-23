package com.unfbx.chatgpt.entity.deepseek;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeepSeekCompletionResponse implements Serializable {

    private String id;
    private String object;
    private Long created;
    private String model;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
    private List<Choice> choices;
    private Usage usage;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        private Long index;
        @JsonProperty("finish_reason")
        private String finishReason;
        private Boolean logprobs;
        private Messages message;
        private Messages delta;
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
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;
        @JsonProperty("prompt_cache_hit_tokens")
        private int promptCacheHitTokens;
        @JsonProperty("prompt_cache_miss_tokens")
        private int promptCacheMissTokens;
        @JsonProperty("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;
        @JsonProperty("completion_tokens_details")
        private CompletionTokensDetails completionTokensDetails;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PromptTokensDetails {
        @JsonProperty("cached_tokens")
        private int cachedTokens;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletionTokensDetails {
        @JsonProperty("reasoning_tokens")
        private int reasoningTokens;
    }

}
