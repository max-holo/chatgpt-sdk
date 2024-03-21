package com.unfbx.chatgpt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.constant.OpenAIConst;
import com.unfbx.chatgpt.entity.KeyInfo;
import com.unfbx.chatgpt.entity.billing.BillingUsage;
import com.unfbx.chatgpt.entity.billing.Subscription;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.function.KeyStrategy;
import com.unfbx.chatgpt.interceptor.DefaultOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAiAuthInterceptor;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource.Factory;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OpenAiStreamClient extends OpenAiClient {
    private static final Logger log = LoggerFactory.getLogger(OpenAiStreamClient.class);

    public static OpenAiStreamClient.Builder builder() {
        return new OpenAiStreamClient.Builder();
    }

    public OpenAiStreamClient(OpenAiStreamClient.Builder builder) {
        String openaiHost = "https://api.openai.com/";
        if (StrUtil.isBlank(builder.apiHost)) {
            builder.apiHost = openaiHost;
        }

        this.apiHost = builder.apiHost;
        if (Objects.isNull(builder.keyStrategy)) {
            List<KeyInfo> collect = (List)builder.apiKey.stream().map(KeyInfo::new).collect(Collectors.toList());
            builder.keyStrategy = (new KeyRandomStrategy()).initKey(collect);
        }

        this.keyStrategy = builder.keyStrategy;
        if (Objects.isNull(builder.authInterceptor)) {
            builder.authInterceptor = new DefaultOpenAiAuthInterceptor(this.keyStrategy);
        }

        this.authInterceptor = builder.authInterceptor;
        if (Objects.isNull(builder.okHttpClient)) {
            builder.okHttpClient = this.okHttpClient(this.keyStrategy);
        } else {
            builder.okHttpClient = builder.okHttpClient.newBuilder().addInterceptor(this.authInterceptor).build();
        }

        this.okHttpClient = builder.okHttpClient;
        new OpenAIConst();
        this.openAiApi = (OpenAiApi)(new retrofit2.Retrofit.Builder()).baseUrl(this.apiHost).client(this.okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(JacksonConverterFactory.create()).build().create(OpenAiApi.class);
    }

    public void streamChatCompletion(ChatCompletion chatCompletion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        } else {
            if (!chatCompletion.isStream()) {
                chatCompletion.setStream(true);
            }

            try {
                Factory factory = EventSources.createFactory(this.okHttpClient);
                ObjectMapper mapper = new ObjectMapper();
                String requestBody = mapper.writeValueAsString(chatCompletion);
                Request request = (new okhttp3.Request.Builder()).url(this.apiHost + "v1/chat/completions").post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody)).build();
                factory.newEventSource(request, eventSourceListener);
            } catch (Exception var8) {
                log.error("请求参数解析异常", var8);
            }

        }
    }

    public Subscription subscription() {
        Single<Subscription> subscription = this.openAiApi.subscription();
        return (Subscription)subscription.blockingGet();
    }

    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate) {
        Single<BillingUsage> billingUsage = this.openAiApi.billingUsage(starDate, endDate);
        return (BillingUsage)billingUsage.blockingGet();
    }

    public static final class Builder {
        private List<String> apiKey;
        private String apiHost;
        private OkHttpClient okHttpClient;
        private KeyStrategy keyStrategy;
        private OpenAiAuthInterceptor authInterceptor;

        public Builder() {
        }

        public OpenAiStreamClient.Builder apiHost(String val) {
            this.apiHost = val;
            return this;
        }

        public OpenAiStreamClient.Builder apiKey(@NotNull List<String> val) {
            this.apiKey = val;
            return this;
        }

        public OpenAiStreamClient.Builder keyStrategy(KeyStrategy val) {
            this.keyStrategy = val;
            return this;
        }

        public OpenAiStreamClient.Builder okHttpClient(OkHttpClient val) {
            this.okHttpClient = val;
            return this;
        }

        public OpenAiStreamClient.Builder authInterceptor(OpenAiAuthInterceptor val) {
            this.authInterceptor = val;
            return this;
        }

        public OpenAiStreamClient build() {
            return new OpenAiStreamClient(this);
        }
    }
}
