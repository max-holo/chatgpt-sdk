package com.unfbx.chatgpt;


import com.unfbx.chatgpt.entity.weixin.*;

public class BaiduAiClient {

    protected BaiduWenXinAiApi baiduWenXinAiApi;

    public ResponseToken getAccessToken(String client_id, String client_secret) {
        return this.baiduWenXinAiApi.getToken("client_credentials", client_id, client_secret).blockingGet();
    }

    public ResponseToken getAccessToken(String grant_type, String client_id, String client_secret) {
        return this.baiduWenXinAiApi.getToken(grant_type, client_id, client_secret).blockingGet();
    }

    public BaiduCompletionResponse completions(BaiduCompletion baiduCompletion) {
        return this.baiduWenXinAiApi.completions(baiduCompletion).blockingGet();
    }
    public BaiduCompletionResponse completions_pro(BaiduCompletion baiduCompletion) {
        return this.baiduWenXinAiApi.completions_pro(baiduCompletion).blockingGet();
    }
    public EmbeddingResponse embedding_V1(EmbeddingRequest embeddingRequest) {
        return this.baiduWenXinAiApi.embedding_V1(embeddingRequest).blockingGet();
    }
}
