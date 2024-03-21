package com.unfbx.chatgpt.entity.weixin;

import com.unfbx.chatgpt.entity.common.Usage;
import com.unfbx.chatgpt.entity.embeddings.Item;
import lombok.Data;

import java.util.List;

@Data
public class EmbeddingResponse {
    private String id;
    private String object;
    private List<Item> data;
    private int created;
    private Usage usage;
    private String error_code;
    private String error_msg;

    public EmbeddingResponse() {
    }
}
