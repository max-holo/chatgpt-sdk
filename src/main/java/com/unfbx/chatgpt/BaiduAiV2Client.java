package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.weixin.EmbeddingRequest;
import com.unfbx.chatgpt.entity.weixin.EmbeddingResponse;
import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionRequest;
import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionResponse;
import com.unfbx.chatgpt.entity.weixinV2.Model;

public class BaiduAiV2Client {

    protected BaiduAiV2Api baiduAiV2Api;

    public BaiduV2CompletionResponse completions(BaiduV2CompletionRequest baiduV2CompletionRequest) {
        return this.baiduAiV2Api.completions(baiduV2CompletionRequest).blockingGet();
    }
    public EmbeddingResponse embedding(EmbeddingRequest embeddingRequest) {
        embeddingRequest.setModel(Model.EMBEDDING_V1.getCode());
        return this.baiduAiV2Api.embedding(embeddingRequest).blockingGet();
    }
}
