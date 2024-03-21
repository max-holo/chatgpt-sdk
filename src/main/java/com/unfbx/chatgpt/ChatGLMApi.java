package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionRequest;
import com.unfbx.chatgpt.entity.chatglm.ChatglmCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatGLMApi {

    @POST("/api/paas/v4/chat/completions")
    Single<ChatglmCompletionResponse> completions(@Body ChatglmCompletionRequest var1);
}