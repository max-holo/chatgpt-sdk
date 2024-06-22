package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.qwen.QwenCompletionRequest;
import com.unfbx.chatgpt.entity.qwen.QwenCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface QwenApi {

    @POST("/api/v1/services/aigc/text-generation/generation")
    Single<QwenCompletionResponse> completions(@Body QwenCompletionRequest var1);
}