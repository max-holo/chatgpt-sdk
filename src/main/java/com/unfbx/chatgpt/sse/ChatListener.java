package com.unfbx.chatgpt.sse;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.sparkEntiy.AIChatRequest;
import com.unfbx.chatgpt.entity.sparkEntiy.AIChatResponse;
import com.unfbx.chatgpt.entity.sparkEntiy.OutHeader.Code;
import com.unfbx.chatgpt.entity.sparkEntiy.Status;
import com.unfbx.chatgpt.entity.sparkEntiy.Usage;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public abstract class ChatListener extends WebSocketListener {
    private static final Logger log = LoggerFactory.getLogger(ChatListener.class);
    private AIChatRequest aiChatRequest;

    public ChatListener(AIChatRequest aiChatRequest) {
        this.aiChatRequest = aiChatRequest;
    }

    public void onWebSocketError(Throwable t, Response response) {
        log.error("调用星火模型时，WebSocket发生异常:{}", response);
        t.printStackTrace();
    }

    public abstract void onChatError(AIChatResponse var1);

    public abstract void onChatOutput(AIChatResponse var1);

    public abstract void onChatEnd();

    public abstract void onChatToken(Usage var1);

    public AIChatRequest onChatSend() {
        return this.aiChatRequest;
    }

    public final void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
    }

    public final void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosing(webSocket, code, reason);
    }

    public final void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
        log.error("---------------> 异常连接中断");
        webSocket.close(1000, "");
        this.onWebSocketError(t, response);
    }

    public final void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        AIChatResponse aiChatResponse = (AIChatResponse)JSONUtil.toBean(text, AIChatResponse.class);
        if (Code.SUCCESS.getValue() != aiChatResponse.getHeader().getCode()) {
            log.warn("调用星火模型发生错误，错误码为：{}，请求的sid为：{}", aiChatResponse.getHeader().getCode(), aiChatResponse.getHeader().getSid());
            webSocket.close(1000, "星火模型调用异常");
            this.onChatError(aiChatResponse);
        } else {
            this.onChatOutput(aiChatResponse);
            if (Status.END.getValue() == aiChatResponse.getHeader().getStatus()) {
                webSocket.close(1000, "星火模型返回结束");
                Usage usage = aiChatResponse.getPayload().getUsage();
                this.onChatToken(usage);
                this.onChatEnd();
            }

        }
    }

    public final void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    public final void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
        try {
            super.onOpen(webSocket, response);
            AIChatRequest aiChatRequest = this.onChatSend();
            ObjectMapper mapper = new ObjectMapper();
            webSocket.send(mapper.writeValueAsString(Objects.isNull(aiChatRequest) ? this.getAiChatRequest() : aiChatRequest));
        } catch (Throwable var5) {
            try {
                throw var5;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public AIChatRequest getAiChatRequest() {
        return this.aiChatRequest;
    }
}
