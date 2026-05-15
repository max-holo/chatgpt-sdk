package com.unfbx.chatgpt.entity.deepseek;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionRequest;
import lombok.*;
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
public class DeepSeekCompletionRequest {

    /**
     * 请求ID
     */
    @JsonProperty("request_id")
    private String requestId = String.format("max-%d", System.currentTimeMillis());

    /**
     * 模型
     */
    private String model = Model.DEEPSEEK_V4_PRO.getCode();
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
     * 惩罚性
     */
    private float frequency_penalty = 0.1f;

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
    private Integer maxTokens = 20000;


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
            if (Model.DEEPSEEK_V4_PRO.getCode().equals(this.model)) {
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
                return new ObjectMapper().writeValueAsString(paramsMap);
            }
            // 默认
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("temperature", temperature);
            paramsMap.put("top_p", topP);
            return new ObjectMapper().writeValueAsString(paramsMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
