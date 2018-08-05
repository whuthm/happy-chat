package com.whuthm.happychat.util;

import java.util.concurrent.atomic.AtomicLong;

public final class PacketIdGenerator {

    private static final String PREFIX = StringUtils.randomString(5) + "-";

    private static final AtomicLong ID = new AtomicLong();

    private PacketIdGenerator() {

    }

    public static String nextId() {
        return PREFIX + Long.toString(ID.incrementAndGet());
    }

}
