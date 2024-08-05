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
    /**
     * 说明：返回flag表示触发安全
     */
    private Integer flag;
    /**
     * 当need_clear_history为true时，此字段会告知第几轮对话有敏感信息，如果是当前问题，ban_round=-1
     */
    private Integer ban_round;

    @Data
    public static class Usage {
        private Integer prompt_tokens;
        private Integer completion_tokens;
        private Integer total_tokens;

    }
}
