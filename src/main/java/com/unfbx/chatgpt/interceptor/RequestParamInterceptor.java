package com.unfbx.chatgpt.interceptor;

import com.unfbx.chatgpt.function.AccessKeyHandler;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

public abstract class RequestParamInterceptor implements Interceptor {
    protected AccessKeyHandler accessKeyHandler;

    public RequestParamInterceptor(AccessKeyHandler accessKeyHandler) {
        this.accessKeyHandler = accessKeyHandler;
    }

    abstract String urlParam(HttpUrl var1);
}

