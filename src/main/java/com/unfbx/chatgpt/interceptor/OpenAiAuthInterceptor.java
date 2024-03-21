package com.unfbx.chatgpt.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.entity.KeyInfo;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletion.Model;
import com.unfbx.chatgpt.entity.chat.ChatCompletion.ModelType;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyStrategy;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

import java.util.List;

public abstract class OpenAiAuthInterceptor implements Interceptor {
    protected KeyStrategy keyStrategy;

    public OpenAiAuthInterceptor(KeyStrategy keyStrategy) {
        this.keyStrategy = keyStrategy;
    }

    protected void onErrorDealApiKeys(String key, String desc) {
        this.keyStrategy.removeErrorKey(key, desc);
    }

    protected static String requestBodyToString(RequestBody requestBody) {
        try {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            return buffer.readUtf8();
        } catch (Exception var2) {
            return "";
        }
    }

    public final KeyInfo getKeyInfo(Request original) {
        List<KeyInfo> keys = this.keyStrategy.getKeys((ModelType)null);
        if (CollectionUtil.isEmpty(keys)) {
            this.keyStrategy.keysWarring();
            throw new BaseException(CommonError.NO_ACTIVE_API_KEYS);
        } else {
            RequestBody body = original.body();
            String method = original.method();
            if ("POST".equals(method) && body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType == null) {
                    return this.keyStrategy.strategy((ModelType)null);
                } else {
                    String subtype = mediaType.subtype();
                    String type = mediaType.type();
                    if ("application".equals(type) && "json".equals(subtype)) {
                        String requestBody = requestBodyToString(body);
                        if (!StrUtil.isBlank(requestBody) && JSONUtil.isTypeJSON(requestBody)) {
                            ChatCompletion chatCompletion = (ChatCompletion)JSONUtil.toBean(requestBody, ChatCompletion.class);
                            if (chatCompletion == null) {
                                return this.keyStrategy.strategy((ModelType)null);
                            } else {
                                String model = chatCompletion.getModel();
                                ModelType modelEnum = Model.getModelTypeEnum(model);
                                return this.keyStrategy.strategy(modelEnum);
                            }
                        } else {
                            return this.keyStrategy.strategy((ModelType)null);
                        }
                    } else {
                        return this.keyStrategy.strategy((ModelType)null);
                    }
                }
            } else {
                return this.keyStrategy.strategy((ModelType)null);
            }
        }
    }

    public OpenAiAuthInterceptor.AuthInfo auth(Request original) {
        KeyInfo keyInfo = this.getKeyInfo(original);
        String key = keyInfo.getKey();
        String apiHost = keyInfo.getApiHost();
        RequestBody body = original.body();
        Request request = original.newBuilder().url(StrUtil.isBlank(apiHost) ? original.url() : original.url().newBuilder().host(apiHost).build()).header(Header.AUTHORIZATION.getValue(), "Bearer " + key).header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue()).method(original.method(), body).build();
        OpenAiAuthInterceptor.AuthInfo authInfo = new OpenAiAuthInterceptor.AuthInfo();
        authInfo.setKey(key);
        authInfo.setRequest(request);
        authInfo.setRequestBody(body);
        return authInfo;
    }

    public static class AuthInfo {
        private String key;
        private Request request;
        private RequestBody requestBody;

        public AuthInfo() {
        }

        public String getKey() {
            return this.key;
        }

        public Request getRequest() {
            return this.request;
        }

        public RequestBody getRequestBody() {
            return this.requestBody;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public void setRequestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
        }

    }
}
