package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionRequest;
import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionResponse;

public class DouBaoAiV3Client {

    protected DouBaoAiV3Api douBaoAiV3Api;

    public BaiduV2CompletionResponse completions(BaiduV2CompletionRequest baiduV2CompletionRequest) {
        return this.douBaoAiV3Api.completions(baiduV2CompletionRequest).blockingGet();
    }

}
