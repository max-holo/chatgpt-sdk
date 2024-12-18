package com.unfbx.chatgpt.interceptor;


import cn.hutool.http.Header;
import cn.hutool.json.JSONObject;
import com.unfbx.chatgpt.function.AccessKeyHandler;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class QwenParamInterceptor extends RequestParamInterceptor {
    public QwenParamInterceptor(AccessKeyHandler accessKeyHandler) {
        super(accessKeyHandler);
    }

    String urlParam(HttpUrl url) {
        return "";
    }

    @NotNull
    public Response intercept(Chain chain) throws IOException {
        String accessKey = super.accessKeyHandler.getAccessKey();
        Request original = chain.request();
        String url = original.url().toString();
        RequestBody body = original.body();

        // 获取请求体内容
        Buffer buffer = new Buffer();
        body.writeTo(buffer);
        String requestBodyString = buffer.readUtf8();

        // 解析 JSON
        JSONObject jsonObject = new JSONObject(requestBodyString);
        boolean incrementalOutput = jsonObject.getJSONObject("parameters").getBool("incremental_output");

        Request request = original.newBuilder()
                .url(url)
                .header(Header.CONTENT_TYPE.getValue(), "application/json; charset=utf-8")
                .header(Header.ACCEPT.getValue(), incrementalOutput ? "text/event-stream" : "*/*")
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + accessKey)
                .method(original.method(), body).build();
        return chain.proceed(request);
    }
}

