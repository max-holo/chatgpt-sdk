package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.baiduAgent.AppBuilderClientResponse;
import com.unfbx.chatgpt.entity.baiduAgent.AppBuilderClientRunRequest;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BaiduAgentApi {

    @POST("/v2/app/conversation/runs")
    Single<AppBuilderClientResponse> conversation_runs(@Body AppBuilderClientRunRequest var1);

}