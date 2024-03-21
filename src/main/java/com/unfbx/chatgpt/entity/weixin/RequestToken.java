package com.unfbx.chatgpt.entity.weixin;

import lombok.Data;

@Data
public class RequestToken {
    private String grant_type;
    private String client_id;
    private String client_secret;

}
