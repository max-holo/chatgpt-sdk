package com.unfbx.chatgpt.entity.baiduAgent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class AppBuilderClientRunRequest {
    @JsonProperty("app_id")
    private String appId;
    private String query;
    private boolean stream;
    @JsonProperty("conversation_id")
    private String conversationID;
    @JsonProperty("end_user_id")
    private String endUserId;
    private Tool[] tools;
    @JsonProperty("tool_outputs")
    private ToolOutput[] ToolOutputs;
    @JsonProperty("tool_choice")
    private ToolChoice ToolChoice;
    private Action action;

    public AppBuilderClientRunRequest() {
    }

    public AppBuilderClientRunRequest(String appID) {
        this.appId = appID;
    }

    public AppBuilderClientRunRequest(String appID, String conversationID) {
        this.appId = appID;
        this.conversationID = conversationID;
    }

    public AppBuilderClientRunRequest(String appID, String conversationID, String query, Boolean stream) {
        this.appId = appID;
        this.conversationID = conversationID;
        this.query = query;
        this.stream = stream;
    }

    @Data
    public static class Tool {
        private String type;
        private Function function;

        public Tool(String type, Function function) {
            this.type = type;
            this.function = function;
        }

        @Data
        public static class Function {
            private String name;
            private String description;
            private Map<String, Object> parameters;

            public Function(String name, String description, Map<String, Object> parameters) {
                this.name = name;
                this.description = description;
                this.parameters = parameters;
            }
        }
    }

    @Data
    public static class ToolOutput {
        @JsonProperty("tool_call_id")
        private String toolCallID;
        private String output;

        public ToolOutput(String toolCallID, String output) {
            this.toolCallID = toolCallID;
            this.output = output;
        }
    }

    @Data
    public static class ToolChoice {
        private String type;
        private Function function;

        public ToolChoice(String type, Function function) {
            this.type = type;
            this.function = function;
        }

        @Data
        public static class Function {
            private String name;
            private Map<String, Object> input;

            public Function(String name, Map<String, Object> input) {
                this.name = name;
                this.input = input;
            }
        }
    }

    @Data
    public static class Action {
        @JsonProperty("action_type")
        private String actionType;
        private Parameters parameters;

        // 回复消息节点构造方法
        public static Action createAction(String interruptId) {
            return createAction("resume", interruptId, "chat");
        }

        public static Action createAction(String actionType, String id, String type) {
            Parameters.InterruptEvent interruptEvent = new Parameters.InterruptEvent(id, type);
            Parameters parameters = new Parameters(interruptEvent);
            return new Action(actionType, parameters);
        }

        public Action(String actionType, Parameters parameters) {
            this.actionType = actionType;
            this.parameters = parameters;
        }

        @Data
        public static class Parameters {
            @JsonProperty("interrupt_event")
            private InterruptEvent interruptEvent;

            public Parameters(InterruptEvent interruptEvent) {
                this.interruptEvent = interruptEvent;
            }

            @Data
            public static class InterruptEvent {
                private String id;
                private String type;

                public InterruptEvent(String id, String type) {
                    this.id = id;
                    this.type = type;
                }
            }
        }
    }
}
