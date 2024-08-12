package com.unfbx.chatgpt.entity.qwen;

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
public class QwenCompletionRequest {

    /**
     * 模型
     */
    private String model = Model.QWEN_MAX.getCode();

    /**
     * messages
     */
    private InputParam input;

    /**
     * 配置参数
     */
    private Parameters parameters;

    @Data
    public static class InputParam {
        private List<Message> messages;
    }

    @Data
    public static class Parameters {
        /**
         * 使用同步调用时，此参数应当设置为 Fasle 或者省略。表示模型生成完所有内容后一次性返回所有内容。
         * 如果设置为 True，模型将通过标准 Event Stream ，逐块返回模型生成内容。Event Stream 结束时会返回一条data: [DONE]消息。
         */
        private Boolean stream = false;
        /**
         * 控制温度【随机性】
         */
        private float temperature = 0.8f;
        /**
         * 多样性控制；
         */
        @JsonProperty("top_p")
        private float topP = 0.8f;
        /**
         * 模型输出最大tokens
         */
        @JsonProperty("max_tokens")
        private Integer maxTokens = 2000;

        @JsonProperty("result_format")
        private String resultFormat = "message";

        /**
         * 联网搜索开关
         */
        @JsonProperty("enable_search")
        private boolean enableSearch = true;

        /**
         * 控制在流式输出模式下是否开启增量输出(仅在流式输出需要传)
         * 和默认值不同时才序列化
         */
//        @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
        @JsonProperty("incremental_output")
        private boolean incrementalOutput = false;

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
            if (Model.QWEN_MAX.getCode().equals(this.model) || Model.QWEN_TURBO.getCode().equals(this.model)
                    || Model.QWEN_PLUS.getCode().equals(this.model)) {
                Map<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("model", this.model);
                if (null == this.input) {
                    throw new RuntimeException("One of messages or prompt must not be empty！");
                }
                paramsMap.put("input", this.input);
                paramsMap.put("parameters", this.parameters);
                return new ObjectMapper().writeValueAsString(paramsMap);
            }
            throw new RuntimeException("the model is not support！");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
