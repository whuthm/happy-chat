package com.whuthm.happychat.common.util;

public interface Mapper<FROM, TO> {

    TO transform(FROM from);
}
