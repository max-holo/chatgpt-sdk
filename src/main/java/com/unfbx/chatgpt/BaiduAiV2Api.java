package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionRequest;
import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaiduAiV2Api {

    @POST("/chat/completions")
    Single<BaiduV2CompletionResponse> completions(@Body BaiduV2CompletionRequest var1);
}