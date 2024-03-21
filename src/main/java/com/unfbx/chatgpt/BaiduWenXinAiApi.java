package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.weixin.*;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BaiduWenXinAiApi {
    String BASE_URL = "https://aip.baidubce.com";
    String COMPLETIONS_URL = "/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";

    @POST("/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions")
    Single<BaiduCompletionResponse> completions(@Body BaiduCompletion var1);

    @POST("/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions_pro")
    Single<BaiduCompletionResponse> completions_pro(@Body BaiduCompletion var1);

    @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/embeddings/embedding-v1")
    Single<EmbeddingResponse> embedding_V1(@Body EmbeddingRequest var1);

    @GET("/oauth/2.0/token")
    Single<ResponseToken> getToken(@Query("grant_type") String var1, @Query("client_id") String var2, @Query("client_secret") String var3);
}