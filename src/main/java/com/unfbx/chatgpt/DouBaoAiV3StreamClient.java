package com.unfbx.chatgpt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.weixinV2.BaiduV2CompletionRequest;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.RequestParamInterceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DouBaoAiV3StreamClient extends DouBaoAiV3Client {

    private static final Logger log = LoggerFactory.getLogger(DouBaoAiV3StreamClient.class);
    private final OkHttpClient okHttpClient;

    public void streamChatCompletion(BaiduV2CompletionRequest baiduV2CompletionRequest, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        } else {
            if (baiduV2CompletionRequest.getStream() == null) {
                baiduV2CompletionRequest.setStream(true);
            }

            try {
                EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
                ObjectMapper mapper = new ObjectMapper();
                String requestBody = baiduV2CompletionRequest.toString();
                Request request = (new Request.Builder()).url("https://ark.cn-beijing.volces.com/api/v3/chat/completions")
                        .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody)).build();
                factory.newEventSource(request, eventSourceListener);
            } catch (Exception var7) {
                log.error("请求参数解析异常", var7);
            }

        }
    }

    public static DouBaoAiV3StreamClient.Builder builder() {
        return new DouBaoAiV3StreamClient.Builder();
    }

    public DouBaoAiV3StreamClient(DouBaoAiV3StreamClient.Builder builder) {
        OkHttpClient okHttpClient = builder.okHttpClient;
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        RequestParamInterceptor requestParamInterceptor = builder.requestParamInterceptor;
        if (okHttpClient == null) {
            okHttpClient = (new OkHttpClient.Builder())
                    .addInterceptor(requestParamInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
//                    .addInterceptor(new CurlInterceptor(new Loggable() {
//                        @Override
//                        public void log(String s) {
//                            System.out.println(s);
//                        }
//                    }))
                    .connectTimeout(30L, TimeUnit.SECONDS).writeTimeout(300L, TimeUnit.SECONDS).readTimeout(300L, TimeUnit.SECONDS)
                    .callTimeout(300L, TimeUnit.SECONDS).build();
        } else if (CollUtil.isEmpty(okHttpClient.interceptors())) {
            okHttpClient = okHttpClient.newBuilder()
                    .addInterceptor(requestParamInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
//                    .addInterceptor(new CurlInterceptor(new Loggable() {
//                        @Override
//                        public void log(String s) {
//                            System.out.println(s);
//                        }
//                    }))
                    .build();
        }

        this.okHttpClient = okHttpClient;
        super.douBaoAiV3Api = (new retrofit2.Retrofit.Builder()).baseUrl("https://ark.cn-beijing.volces.com/api").client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(JacksonConverterFactory.create()).build().create(DouBaoAiV3Api.class);
    }

    public static final class Builder {
        private OkHttpClient okHttpClient;
        private RequestParamInterceptor requestParamInterceptor;

        public Builder() {
        }

        public DouBaoAiV3StreamClient.Builder paramInterceptor(RequestParamInterceptor val) {
            this.requestParamInterceptor = val;
            return this;
        }

        public DouBaoAiV3StreamClient.Builder okHttpClient(OkHttpClient val) {
            val.newBuilder().callTimeout(30, TimeUnit.SECONDS).connectTimeout(90L, TimeUnit.SECONDS)
                    .writeTimeout(90L, TimeUnit.SECONDS).readTimeout(90L, TimeUnit.SECONDS).build();
            this.okHttpClient = val;
            return this;
        }

        public DouBaoAiV3StreamClient build() {
            return new DouBaoAiV3StreamClient(this);
        }
    }
}
