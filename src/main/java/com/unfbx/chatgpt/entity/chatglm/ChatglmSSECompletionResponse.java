package com.unfbx.chatgpt.entity.chatglm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class ChatglmSSECompletionResponse implements Serializable {

    private String id;
    @JsonProperty("request_id")
    private String requestId;
    private Long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;

    @Data
    public static class Choice {
        private Long index;
        @JsonProperty("finish_reason")
        private String finishReason;
        private Messages delta;
    }

    @Data
    public static class Messages {
        private String role;
        private String content;
    }

    @Data
    public static class Usage {
        private int completion_tokens;
        private int prompt_tokens;
        private int total_tokens;
    }

}
