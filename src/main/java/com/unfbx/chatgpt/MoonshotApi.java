package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.moonshot.MoonshotCompletionRequest;
import com.unfbx.chatgpt.entity.moonshot.MoonshotCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MoonshotApi {

    @POST("/v1/chat/completions")
    Single<MoonshotCompletionResponse> completions(@Body MoonshotCompletionRequest var1);
}