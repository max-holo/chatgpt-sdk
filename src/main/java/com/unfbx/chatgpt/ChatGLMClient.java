package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionRequest;
import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionResponse;

public class ChatGLMClient {

    protected ChatGLMApi chatGLMApi;

    public ChatglmCompletionResponse completions(ChatglmCompletionRequest chatglmCompletion) {
        return this.chatGLMApi.completions(chatglmCompletion).blockingGet();
    }

}
