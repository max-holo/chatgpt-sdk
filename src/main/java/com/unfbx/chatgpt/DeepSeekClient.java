package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionRequest;
import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionResponse;
import com.unfbx.chatgpt.entity.deepseek.DeepSeekCompletionRequest;
import com.unfbx.chatgpt.entity.deepseek.DeepSeekCompletionResponse;

public class DeepSeekClient {

    protected DeepSeekApi deepSeekApi;

    public DeepSeekCompletionResponse completions(DeepSeekCompletionRequest deepSeekCompletionRequest) {
        return this.deepSeekApi.completions(deepSeekCompletionRequest).blockingGet();
    }

}
