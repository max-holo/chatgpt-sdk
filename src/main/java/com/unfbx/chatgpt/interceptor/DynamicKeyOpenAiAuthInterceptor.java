package com.unfbx.chatgpt.interceptor;

import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyStrategy;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

public class DynamicKeyOpenAiAuthInterceptor extends OpenAiAuthInterceptor {
    private static final Logger log = LoggerFactory.getLogger(DynamicKeyOpenAiAuthInterceptor.class);
    private static final String ACCOUNT_DEACTIVATED = "account_deactivated";
    private static final String INVALID_API_KEY = "invalid_api_key";
    private static final String INSUFFICIENT_QUOTA = "insufficient_quota";

    public DynamicKeyOpenAiAuthInterceptor(KeyStrategy keyStrategy) {
        super(keyStrategy);
    }

    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        AuthInfo auth = this.auth(original);
        Request request = auth.getRequest();
        String key = auth.getKey();
        Response response = chain.proceed(request);
        if (!response.isSuccessful()) {
            String errorMsg = response.body().string();
            OpenAiResponse openAiResponse;
            if (response.code() != CommonError.OPENAI_AUTHENTICATION_ERROR.code() && response.code() != CommonError.OPENAI_LIMIT_ERROR.code() && response.code() != CommonError.OPENAI_SERVER_ERROR.code()) {
                log.error("--------> 请求异常：{}", errorMsg);
                openAiResponse = (OpenAiResponse)JSONUtil.toBean(errorMsg, OpenAiResponse.class);
                if (Objects.nonNull(openAiResponse.getError())) {
                    log.error(openAiResponse.getError().getMessage());
                    throw new BaseException(openAiResponse.getError().getMessage());
                } else {
                    throw new BaseException(CommonError.RETRY_ERROR);
                }
            } else {
                openAiResponse = (OpenAiResponse)JSONUtil.toBean(errorMsg, OpenAiResponse.class);
                String errorCode = openAiResponse.getError().getCode();
                log.error("--------> 请求openai异常，错误code：{}", errorCode);
                log.error("--------> 请求异常：{}", errorMsg);
                if ("account_deactivated".equals(errorCode) || "invalid_api_key".equals(errorCode) || "insufficient_quota".equals(errorCode)) {
                    this.onErrorDealApiKeys(key, errorMsg);
                }

                throw new BaseException(openAiResponse.getError().getMessage());
            }
        } else {
            return response;
        }
    }

    public AuthInfo auth(Request original) {
        return super.auth(original);
    }
}
