package com.unfbx.chatgpt.entity.weixin;

import lombok.Data;

import java.util.List;

/**
 * 向量入参
 * @author max
 */
@Data
public class EmbeddingRequest {
    private List<String> input;
    private String user_id;
}
