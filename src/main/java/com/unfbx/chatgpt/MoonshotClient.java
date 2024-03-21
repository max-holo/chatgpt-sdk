package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.moonshot.MoonshotCompletionRequest;
import com.unfbx.chatgpt.entity.moonshot.MoonshotCompletionResponse;

public class MoonshotClient {

    protected MoonshotApi moonshotApi;

    public MoonshotCompletionResponse completions(MoonshotCompletionRequest moonshotCompletion) {
        return this.moonshotApi.completions(moonshotCompletion).blockingGet();
    }

}
