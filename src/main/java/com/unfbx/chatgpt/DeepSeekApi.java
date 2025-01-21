package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.deepseek.DeepSeekCompletionRequest;
import com.unfbx.chatgpt.entity.deepseek.DeepSeekCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DeepSeekApi {

    @POST("/chat/completions")
    Single<DeepSeekCompletionResponse> completions(@Body DeepSeekCompletionRequest var1);
}