package com.unfbx.chatgpt.entity.sparkEntiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
@Builder
public class InHeader {
    @JsonProperty("app_id")
    private String appid;
    private String uid;

}

