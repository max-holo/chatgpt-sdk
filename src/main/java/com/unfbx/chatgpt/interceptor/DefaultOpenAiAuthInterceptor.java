package com.unfbx.chatgpt.interceptor;

import com.unfbx.chatgpt.function.KeyStrategy;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DefaultOpenAiAuthInterceptor extends OpenAiAuthInterceptor {
    private static final Logger log = LoggerFactory.getLogger(DefaultOpenAiAuthInterceptor.class);

    public DefaultOpenAiAuthInterceptor(KeyStrategy keyStrategy) {
        super(keyStrategy);
    }

    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        return chain.proceed(this.auth(original).getRequest());
    }
}
