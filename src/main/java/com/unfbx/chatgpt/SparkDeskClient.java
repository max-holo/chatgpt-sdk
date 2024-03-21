package com.unfbx.chatgpt;


import com.unfbx.chatgpt.sse.ChatListener;
import com.unfbx.chatgpt.utils.SparkAuthUtils;
import lombok.Builder;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Data
@Builder
public class SparkDeskClient {
    private String host;
    private String appid;
    private String apiKey;
    private String apiSecret;
    private OkHttpClient okHttpClient;


    public <T extends ChatListener> WebSocket chat(T chatListener) {
        try {
            String authUrl = SparkAuthUtils.getAuthUrl(this.host, this.apiKey, this.apiSecret);
            String url = authUrl.replace("http://", "ws://").replace("https://", "wss://");
            Request request = (new okhttp3.Request.Builder()).url(url).build();
            WebSocket webSocket = this.okHttpClient.newWebSocket(request, chatListener);
            return webSocket;
        } catch (Exception var6) {
            try {
                throw var6;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
