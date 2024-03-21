package com.unfbx.chatgpt.entity.sparkEntiy;

public enum Status {
    START(0),
    ING(1),
    END(2);

    private final int value;

    private Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
