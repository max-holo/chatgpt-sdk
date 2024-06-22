package com.unfbx.chatgpt.entity.weixin;

import lombok.Data;

import java.util.List;

@Data
public class BaiduCompletion {
    private List<Message> messages;
    private Boolean stream;
    private String user_id;
    private Double temperature;
    private Double top_p;
    private Double penalty_score;
    private Integer max_output_tokens = 2048;
    /**
     * 指定响应内容的格式，说明：
     * （1）可选值：
     * · json_object：以json格式返回，可能出现不满足效果情况
     * · text：以文本格式返回
     * （2）如果不填写参数response_format值，默认为text
     */
    private String response_format;

    @Data
    public static class Message {
        private String role;
        private String content;

    }
}
