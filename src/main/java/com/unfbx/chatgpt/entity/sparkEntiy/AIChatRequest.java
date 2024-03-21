package com.unfbx.chatgpt.entity.sparkEntiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
@Builder
public class AIChatRequest {
    private InHeader header;
    private Parameter parameter;
    private InPayload payload;
}

