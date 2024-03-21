package com.unfbx.chatgpt.entity.weixin;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaiduCompletionResponse implements Serializable {
    private String id;
    private String object;
    private Integer created;
    private Integer sentence_id;
    private Boolean is_end;
    private String result;
    private String finish_reason;
    private Boolean is_truncated;
    private Boolean need_clear_history;
    private BaiduCompletionResponse.Usage usage;
    private String error_code;
    private String error_msg;

    @Data
    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;

    }
}
