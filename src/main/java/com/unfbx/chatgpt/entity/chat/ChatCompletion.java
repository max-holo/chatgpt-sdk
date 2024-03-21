package com.unfbx.chatgpt.entity.chat;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletion implements Serializable {

    @NonNull
    @Builder.Default
    private String model = Model.GPT_3_5_TURBO.getName();
    /**
     * 问题描述
     */
    @NonNull
    private List<Message> messages;

    private List<Functions> functions;

    /**
     * 取值：null,auto或者自定义
     * functions没有值的时候默认为：null
     * functions存在值得时候默认为：auto
     * 也可以自定义
     */
    @JsonProperty("function_call")
    private Object functionCall;

    /**
     * 使用什么取样温度，0到2之间。较高的值(如0.8)将使输出更加随机，而较低的值(如0.2)将使输出更加集中和确定。
     * <p>
     * We generally recommend altering this or but not both.top_p
     */
    @Builder.Default
    private double temperature = 0.2;

    /**
     * 使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币。
     * <p>
     * 我们通常建议更改此设置，但不要同时更改两者。temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    private Double topP = 1d;


    /**
     * 为每个提示生成的完成次数。
     */
    @Builder.Default
    private Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     *
     * @see com.unfbx.chatgpt.OpenAiStreamClient
     */
    @Builder.Default
    private boolean stream = false;
    /**
     * 停止输出标识
     */
    private List<String> stop;
    /**
     * 最大支持4096
     */
    @JsonProperty("max_tokens")
    @Builder.Default
    private Integer maxTokens = 2048;


    @JsonProperty("presence_penalty")
    @Builder.Default
    private double presencePenalty = 0;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    @Builder.Default
    private double frequencyPenalty = 0;

    @JsonProperty("logit_bias")
    private Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;

    /**
     * 获取当前参数的tokens数
     */
    public long tokens() {
        if (CollectionUtil.isEmpty(this.messages) || StrUtil.isBlank(this.model)) {
            log.warn("参数异常model：{}，prompt：{}", this.model, this.messages);
            return 0;
        }
        return TikTokensUtil.tokens(this.model, this.messages);
    }

    public static class ChatCompletionBuilder {
        private boolean model$set;
        private String model$value;
        private List<Message> messages;
        private List<Functions> functions;
        private Object functionCall;
        private boolean temperature$set;
        private double temperature$value;
        private boolean topP$set;
        private Double topP$value;
        private boolean n$set;
        private Integer n$value;
        private boolean stream$set;
        private boolean stream$value;
        private List<String> stop;
        private boolean maxTokens$set;
        private Integer maxTokens$value;
        private boolean presencePenalty$set;
        private double presencePenalty$value;
        private boolean frequencyPenalty$set;
        private double frequencyPenalty$value;
        private Map logitBias;
        private String user;

        ChatCompletionBuilder() {
        }

        public ChatCompletion.ChatCompletionBuilder model(@NonNull String model) {
            if (model == null) {
                throw new NullPointerException("model is marked non-null but is null");
            } else {
                this.model$value = model;
                this.model$set = true;
                return this;
            }
        }

        public ChatCompletion.ChatCompletionBuilder messages(@NonNull List<Message> messages) {
            if (messages == null) {
                throw new NullPointerException("messages is marked non-null but is null");
            } else {
                this.messages = messages;
                return this;
            }
        }

        public ChatCompletion.ChatCompletionBuilder functions(List<Functions> functions) {
            this.functions = functions;
            return this;
        }

        @JsonProperty("function_call")
        public ChatCompletion.ChatCompletionBuilder functionCall(Object functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public ChatCompletion.ChatCompletionBuilder temperature(double temperature) {
            this.temperature$value = temperature;
            this.temperature$set = true;
            return this;
        }

        @JsonProperty("top_p")
        public ChatCompletion.ChatCompletionBuilder topP(Double topP) {
            this.topP$value = topP;
            this.topP$set = true;
            return this;
        }

        public ChatCompletion.ChatCompletionBuilder n(Integer n) {
            this.n$value = n;
            this.n$set = true;
            return this;
        }

        public ChatCompletion.ChatCompletionBuilder stream(boolean stream) {
            this.stream$value = stream;
            this.stream$set = true;
            return this;
        }

        public ChatCompletion.ChatCompletionBuilder stop(List<String> stop) {
            this.stop = stop;
            return this;
        }

        @JsonProperty("max_tokens")
        public ChatCompletion.ChatCompletionBuilder maxTokens(Integer maxTokens) {
            this.maxTokens$value = maxTokens;
            this.maxTokens$set = true;
            return this;
        }

        @JsonProperty("presence_penalty")
        public ChatCompletion.ChatCompletionBuilder presencePenalty(double presencePenalty) {
            this.presencePenalty$value = presencePenalty;
            this.presencePenalty$set = true;
            return this;
        }

        @JsonProperty("frequency_penalty")
        public ChatCompletion.ChatCompletionBuilder frequencyPenalty(double frequencyPenalty) {
            this.frequencyPenalty$value = frequencyPenalty;
            this.frequencyPenalty$set = true;
            return this;
        }

        @JsonProperty("logit_bias")
        public ChatCompletion.ChatCompletionBuilder logitBias(Map logitBias) {
            this.logitBias = logitBias;
            return this;
        }

        public ChatCompletion.ChatCompletionBuilder user(String user) {
            this.user = user;
            return this;
        }

        public ChatCompletion build() {
            String model$value = this.model$value;
            if (!this.model$set) {
                model$value = ChatCompletion.$default$model();
            }

            double temperature$value = this.temperature$value;
            if (!this.temperature$set) {
                temperature$value = ChatCompletion.$default$temperature();
            }

            Double topP$value = this.topP$value;
            if (!this.topP$set) {
                topP$value = ChatCompletion.$default$topP();
            }

            Integer n$value = this.n$value;
            if (!this.n$set) {
                n$value = ChatCompletion.$default$n();
            }

            boolean stream$value = this.stream$value;
            if (!this.stream$set) {
                stream$value = ChatCompletion.$default$stream();
            }

            Integer maxTokens$value = this.maxTokens$value;
            if (!this.maxTokens$set) {
                maxTokens$value = ChatCompletion.$default$maxTokens();
            }

            double presencePenalty$value = this.presencePenalty$value;
            if (!this.presencePenalty$set) {
                presencePenalty$value = ChatCompletion.$default$presencePenalty();
            }

            double frequencyPenalty$value = this.frequencyPenalty$value;
            if (!this.frequencyPenalty$set) {
                frequencyPenalty$value = ChatCompletion.$default$frequencyPenalty();
            }

            return new ChatCompletion(model$value, this.messages, this.functions, this.functionCall, temperature$value, topP$value, n$value, stream$value, this.stop, maxTokens$value, presencePenalty$value, frequencyPenalty$value, this.logitBias, this.user);
        }

        public String toString() {
            return "ChatCompletion.ChatCompletionBuilder(model$value=" + this.model$value + ", messages=" + this.messages + ", functions=" + this.functions + ", functionCall=" + this.functionCall + ", temperature$value=" + this.temperature$value + ", topP$value=" + this.topP$value + ", n$value=" + this.n$value + ", stream$value=" + this.stream$value + ", stop=" + this.stop + ", maxTokens$value=" + this.maxTokens$value + ", presencePenalty$value=" + this.presencePenalty$value + ", frequencyPenalty$value=" + this.frequencyPenalty$value + ", logitBias=" + this.logitBias + ", user=" + this.user + ")";
        }
    }

    /**
     * 最新模型参考官方文档：
     * <a href="https://platform.openai.com/docs/models/model-endpoint-compatibility">官方稳定模型列表</a>
     */
    @Getter
    public enum Model {
        GPT_3_5_TURBO("gpt-3.5-turbo", ChatCompletion.ModelType.GPT3),
        GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613", ChatCompletion.ModelType.GPT3),
        GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k", ChatCompletion.ModelType.GPT3),
        GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613", ChatCompletion.ModelType.GPT3),
        GPT_3_5_TURBO_0914("gpt-3.5-turbo-instruct-0914", ChatCompletion.ModelType.GPT3),
        GPT_4("gpt-4", ChatCompletion.ModelType.GPT4),
        GPT_4_32K("gpt-4-32k", ChatCompletion.ModelType.GPT4),
        GPT_4_0613("gpt-4-0613", ChatCompletion.ModelType.GPT4),
        GPT_4_1106_PREVIEW("gpt-4-1106-preview", ChatCompletion.ModelType.GPT4);

        private String name;
        private ChatCompletion.ModelType modelType;

        public static ChatCompletion.ModelType getModelTypeEnum(String model) {
            ChatCompletion.Model[] var1 = values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                ChatCompletion.Model value = var1[var3];
                if (value.getName().equals(model)) {
                    return value.getModelType();
                }
            }

            return null;
        }

        public String getName() {
            return this.name;
        }

        public ChatCompletion.ModelType getModelType() {
            return this.modelType;
        }

        private Model(String name, ChatCompletion.ModelType modelType) {
            this.name = name;
            this.modelType = modelType;
        }
    }

    public static enum ModelType {
        GPT3("3.5"),
        GPT4("4");

        private String type;

        public String getType() {
            return this.type;
        }

        private ModelType(String type) {
            this.type = type;
        }
    }

}
