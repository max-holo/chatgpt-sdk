package com.unfbx.chatgpt.entity.baiduAgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class EventContent {
    public static final String CodeContentType = "code";
    public static final String TextContentType = "text";
    public static final String ImageContentType = "image";
    public static final String RAGContentType = "rag";
    public static final String FunctionCallContentType = "function_call";
    public static final String AudioContentType = "audio";
    public static final String VideoContentType = "video";
    public static final String StatusContentType = "status";
    public static final String ChatflowInterruptContentType = "chatflow_interrupt";
    public static final String PublishMessageContentType = "publish_message";
    public static final String MultipleDialogEventContentType = "multiple_dialog_event";
    public static final String JsonContentType = "json";

    @JsonProperty("event_code")
    private String eventCode;
    @JsonProperty("event_message")
    private String enentMessage;
    @JsonProperty("event_type")
    private String eventType;
    @JsonProperty("event_id")
    private String eventId;
    @JsonProperty("event_status")
    private String eventStatus;
    @JsonProperty("content_type")
    private String contentType;
    private Map<String, Object> outputs;
    private Map<String, Object> usage;
    @JsonProperty("tool_calls")
    private ToolCall[] toolCalls;
}
