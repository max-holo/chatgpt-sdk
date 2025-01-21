package com.unfbx.chatgpt.entity.qwen;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QwenCompletionResponse implements Serializable {

    @JsonProperty("request_id")
    private String requestId;

    private OutPutResult output;

    private Usage usage;

    private String code;

    private String message;

    @Data
    public static class OutPutResult {
        private List<Choice> choices;
    }


    @Data
    public static class Choice {
        @JsonProperty("finish_reason")
        private String finishReason;
        private Messages message;
    }

    @Data
    public static class Messages {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        @JsonProperty("prompt_tokens_details")
        private PromptTokensDetails promptTokensDetails;
        private int input_tokens;
        private int output_tokens;
        private int total_tokens;
    }

    @Data
    public static class PromptTokensDetails {
        private int cached_tokens;
    }

}
