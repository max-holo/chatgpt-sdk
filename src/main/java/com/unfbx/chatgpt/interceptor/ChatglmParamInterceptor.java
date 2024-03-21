package com.unfbx.chatgpt.interceptor;


import cn.hutool.http.Header;
import com.unfbx.chatgpt.function.AccessKeyHandler;
import com.unfbx.chatgpt.utils.BearerTokenUtils;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ChatglmParamInterceptor extends RequestParamInterceptor {
    public ChatglmParamInterceptor(AccessKeyHandler accessKeyHandler) {
        super(accessKeyHandler);
    }

    String urlParam(HttpUrl url) {
        return "";
    }

    @NotNull
    public Response intercept(Chain chain) throws IOException {
        String[] accessKey = super.accessKeyHandler.getAccessKey().split("\\.");
        if (accessKey.length != 2) {
            throw new RuntimeException("invalid apiSecretKey");
        }
        Request original = chain.request();
        String url = original.url().toString();
        RequestBody body = original.body();
        Request request = original.newBuilder()
                .url(url)
                .header(Header.CONTENT_TYPE.getValue(), "application/json; charset=utf-8")
                .header(Header.ACCEPT.getValue(), "*/*")
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + BearerTokenUtils.getToken(accessKey[0], accessKey[1]))
                .method(original.method(), body).build();
        return chain.proceed(request);
    }
}

