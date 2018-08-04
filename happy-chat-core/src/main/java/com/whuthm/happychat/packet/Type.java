package com.whuthm.happychat.packet;

/**
 * Packet Type
 */
public enum  Type {
    iq(1),
    message(2),
    push(3);

    private final int value;

    Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
