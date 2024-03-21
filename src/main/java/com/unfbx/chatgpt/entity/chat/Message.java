package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
public class Message implements Serializable {
    private String role;
    private String content;
    private String name;
    @JsonProperty("function_call")
    private FunctionCall functionCall;

    public static Message.Builder builder() {
        return new Message.Builder();
    }

    public Message(String role, String content, String name, FunctionCall functionCall) {
        this.role = role;
        this.content = content;
        this.name = name;
        this.functionCall = functionCall;
    }

    public Message(String role, String content, String name) {
        this.role = role;
        this.content = content;
        this.name = name;
    }

    public Message() {
    }

    private Message(Message.Builder builder) {
        this.setRole(builder.role);
        this.setContent(builder.content);
        this.setName(builder.name);
        this.setFunctionCall(builder.functionCall);
    }

    public String getRole() {
        return this.role;
    }

    public String getContent() {
        return this.content;
    }

    public String getName() {
        return this.name;
    }

    public FunctionCall getFunctionCall() {
        return this.functionCall;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("function_call")
    public void setFunctionCall(FunctionCall functionCall) {
        this.functionCall = functionCall;
    }


    public static final class Builder {
        private String role;
        private String content;
        private String name;
        private FunctionCall functionCall;

        public Builder() {
        }

        public Message.Builder role(Message.Role role) {
            this.role = role.getName();
            return this;
        }

        public Message.Builder role(String role) {
            this.role = role;
            return this;
        }

        public Message.Builder content(String content) {
            this.content = content;
            return this;
        }

        public Message.Builder name(String name) {
            this.name = name;
            return this;
        }

        public Message.Builder functionCall(FunctionCall functionCall) {
            this.functionCall = functionCall;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }

    public static enum Role {
        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        FUNCTION("function");

        private String name;

        public String getName() {
            return this.name;
        }

        private Role(String name) {
            this.name = name;
        }
    }
}
