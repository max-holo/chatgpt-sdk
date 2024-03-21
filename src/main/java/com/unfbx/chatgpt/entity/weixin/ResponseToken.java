package com.unfbx.chatgpt.entity.weixin;


import lombok.Data;

@Data
public class ResponseToken {
    private String refresh_token;
    private Long expires_in;
    private String session_key;
    private String access_token;
    private String scope;
    private String session_secret;
}
