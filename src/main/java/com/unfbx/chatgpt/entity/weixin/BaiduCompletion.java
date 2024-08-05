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

    /**
     * 是否开启系统记忆，说明：
     * （1）false：未开启，默认false
     * （2）true：表示开启，开启后，system_memory_id字段必填
     */
    private Boolean enable_system_memory = false;

    /**
     * 系统记忆ID，用于读取对应ID下的系统记忆，读取到的记忆文本内容会拼接message参与请求推理
     */
    private String system_memory_id;

    /**
     * 是否强制关闭实时搜索功能，默认false，表示不关闭
     */
    private Boolean disable_search = false;

    /**
     * 重试次数，默认1次
     */
    private Integer retry_count = 1;

    /**
     * 请求超时时间，默认120秒
     */
    private Double request_timeout = 120d;

    /**
     * 请求重试参数，用于指定重试的策略，默认为0
     */
    private Double backoff_factor = 0d;

    @Data
    public static class Message {
        private String role;
        private String content;

    }
}
