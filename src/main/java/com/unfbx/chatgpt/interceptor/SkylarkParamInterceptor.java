package com.unfbx.chatgpt.interceptor;


import cn.hutool.http.Header;
import com.unfbx.chatgpt.function.AccessKeyHandler;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SkylarkParamInterceptor extends RequestParamInterceptor {
    public SkylarkParamInterceptor(AccessKeyHandler accessKeyHandler) {
        super(accessKeyHandler);
    }

    String urlParam(HttpUrl url) {
        return "";
    }

    @NotNull
    public Response intercept(Chain chain) throws IOException {
        String[] accessKey = super.accessKeyHandler.getAccessKey().split("\\.");
        if (accessKey.length != 2) {
            throw new RuntimeException("invalid AccessKey");
        }
        Request original = chain.request();
        String url = original.url().toString();
        RequestBody body = original.body();
        Headers headers = original.headers();
        Request request = original.newBuilder()
                .url(url)
                .header(Header.CONTENT_TYPE.getValue(), "application/json")
                .header(Header.AUTHORIZATION.getValue(), "HMAC-SHA256" +
                        " Credential=" + accessKey[0] + "/" + "credentialScope" +
                        ", SignedHeaders=" + "signHeader" +
                        ", Signature=" + "signature")
                .method(original.method(), body).build();
        return chain.proceed(request);
    }
}

