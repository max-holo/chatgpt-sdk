package com.unfbx.chatgpt.entity.sparkEntiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
@Builder
public class OutHeader {
    private int code;
    private int status;
    private String message;
    private String sid;

    public static enum Code {
        SUCCESS(0);

        private final int value;

        private Code(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }
}

