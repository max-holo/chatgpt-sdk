package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.qwen.QwenCompletionRequest;
import com.unfbx.chatgpt.entity.qwen.QwenCompletionResponse;

public class QwenClient {

    protected QwenApi qwenApi;

    public QwenCompletionResponse completions(QwenCompletionRequest qwenCompletionRequest) {
        return this.qwenApi.completions(qwenCompletionRequest).blockingGet();
    }

}
