package com.unfbx.chatgpt.entity.weixinV2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaiduV2CompletionRequest {

    /**
     * 模型
     */
    private String model = Model.DEEPSEEK_R1.getCode();
    /**
     * 请求参数
     */
    private List<Message> messages;

    /**
     * 使用同步调用时，此参数应当设置为 Fasle 或者省略。表示模型生成完所有内容后一次性返回所有内容。
     * 如果设置为 True，模型将通过标准 Event Stream ，逐块返回模型生成内容。Event Stream 结束时会返回一条data: [DONE]消息。
     */
    private Boolean stream = true;

    /**
     * 温度
     */
    private float temperature = 0.8f;
    /**
     * 多样性控制；
     */
    @JsonProperty("top_p")
    private float topP = 1.0f;
    /**
     * 模型输出最大tokens
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens = 8192;

    /**
     * 联网搜索
     */
    private WebSearch webSearch;

    /**
     * 流式输出时，是否输出usage
     */
    private StreamOptions streamOptions;


    @Data
    public static class WebSearch {
        private boolean enable;
        private boolean enable_citation;
        private boolean enable_trace;
    }

    @Data
    public static class StreamOptions {
        private boolean include_usage = true;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;

        public static MessageBuilder builder() {
            return new MessageBuilder();
        }

        public static class MessageBuilder {
            private String role;
            private String content;

            MessageBuilder() {
            }

            public MessageBuilder role(String role) {
                this.role = role;
                return this;
            }

            public MessageBuilder content(String content) {
                this.content = content;
                return this;
            }

            public Message build() {
                return new Message(this.role, this.content);
            }

        }

    }

    @Override
    public String toString() {
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("model", this.model);
            if (null == this.messages) {
                throw new RuntimeException("One of messages or prompt must not be empty！");
            }
            paramsMap.put("messages", this.messages);
            paramsMap.put("stream", this.stream);
            paramsMap.put("temperature", this.temperature);
            paramsMap.put("top_p", this.topP);
            paramsMap.put("max_tokens", this.maxTokens);
            paramsMap.put("stream_options", this.streamOptions);
            if(null != this.webSearch){
                paramsMap.put("web_search",this.webSearch);
            }
            return new ObjectMapper().writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
