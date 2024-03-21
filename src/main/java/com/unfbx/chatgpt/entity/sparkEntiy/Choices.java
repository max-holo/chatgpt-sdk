package com.unfbx.chatgpt.entity.sparkEntiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
@Builder
public class Choices {
    private Integer status;
    private Integer seq;
    private List<Text> text;
}

