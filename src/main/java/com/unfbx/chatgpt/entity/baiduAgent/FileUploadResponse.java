package com.unfbx.chatgpt.entity.baiduAgent;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUploadResponse {
    private String requestId;
    @JsonProperty("id")
    private String fileId;
    @JsonProperty("conversation_id")
    private String conversationId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFileId() {
        return fileId;
    }

    @Override
    public String toString() {
        return "FileUploadResponse{" +
                "requestId='" + requestId + '\'' +
                ", fileId='" + fileId + '\'' +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
