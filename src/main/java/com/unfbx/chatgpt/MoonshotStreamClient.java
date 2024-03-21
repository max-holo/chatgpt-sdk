package com.unfbx.chatgpt;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.moonshot.MoonshotCompletionRequest;
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

public class MoonshotStreamClient extends MoonshotClient {

    private static final Logger log = LoggerFactory.getLogger(MoonshotStreamClient.class);
    private final OkHttpClient okHttpClient;

    public void streamChatCompletion(MoonshotCompletionRequest moonshotCompletionRequest, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        } else {
            if (moonshotCompletionRequest.getStream() == null) {
                moonshotCompletionRequest.setStream(true);
            }

            try {
                EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
                ObjectMapper mapper = new ObjectMapper();
                String requestBody = moonshotCompletionRequest.toString();
                Request request = (new Request.Builder()).url("https://api.moonshot.cn/v1/chat/completions")
                        .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody)).build();
                factory.newEventSource(request, eventSourceListener);
            } catch (Exception var7) {
                log.error("请求参数解析异常", var7);
            }

        }
    }

    public static MoonshotStreamClient.Builder builder() {
        return new MoonshotStreamClient.Builder();
    }

    public MoonshotStreamClient(MoonshotStreamClient.Builder builder) {
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
                    .connectTimeout(30L, TimeUnit.SECONDS).writeTimeout(90L, TimeUnit.SECONDS).readTimeout(90L, TimeUnit.SECONDS)
                    .callTimeout(90L, TimeUnit.SECONDS).build();
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
        super.moonshotApi = (new retrofit2.Retrofit.Builder()).baseUrl("https://api.moonshot.cn").client(okHttpClient).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(JacksonConverterFactory.create()).build().create(MoonshotApi.class);
    }

    public static final class Builder {
        private OkHttpClient okHttpClient;
        private RequestParamInterceptor requestParamInterceptor;

        public Builder() {
        }

        public MoonshotStreamClient.Builder paramInterceptor(RequestParamInterceptor val) {
            this.requestParamInterceptor = val;
            return this;
        }

        public MoonshotStreamClient.Builder okHttpClient(OkHttpClient val) {
            val.newBuilder().callTimeout(30,TimeUnit.SECONDS).connectTimeout(90L,TimeUnit.SECONDS)
                    .writeTimeout(90L,TimeUnit.SECONDS).readTimeout(90L,TimeUnit.SECONDS).build();
            this.okHttpClient = val;
            return this;
        }

        public MoonshotStreamClient build() {
            return new MoonshotStreamClient(this);
        }
    }
}
