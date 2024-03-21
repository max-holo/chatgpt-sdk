package com.unfbx.chatgpt.entity.sparkEntiy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Text {
    private String role;
    private String content;
    private Integer index;

    public static enum Role {
        USER("user"),
        ASSISTANT("assistant");

        private final String name;

        private Role(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}

