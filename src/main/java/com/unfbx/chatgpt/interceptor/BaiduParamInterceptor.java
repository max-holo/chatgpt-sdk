package com.unfbx.chatgpt.interceptor;


import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.unfbx.chatgpt.function.AccessKeyHandler;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class BaiduParamInterceptor extends RequestParamInterceptor {
    public BaiduParamInterceptor(AccessKeyHandler accessKeyHandler) {
        super(accessKeyHandler);
    }

    String urlParam(HttpUrl url) {
        return url + "?access_token=" + super.accessKeyHandler.getAccessKey();
    }

    @NotNull
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String url = this.urlParam(original.url());
        RequestBody body = original.body();
        Request request = original.newBuilder().url(url).header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue()).method(original.method(), body).build();
        return chain.proceed(request);
    }
}

