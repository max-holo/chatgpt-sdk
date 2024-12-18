package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.baiduAgent.AppBuilderClientResponse;
import com.unfbx.chatgpt.entity.baiduAgent.AppBuilderClientRunRequest;

public class BaiduAgentClient {
    protected BaiduAgentApi baiduAgentApi;
    public AppBuilderClientResponse runs(AppBuilderClientRunRequest appBuilderClientRunRequest) {
        return this.baiduAgentApi.conversation_runs(appBuilderClientRunRequest).blockingGet();
    }

}
