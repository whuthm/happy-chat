package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Message;

import java.util.Comparator;

public class MessageCompator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2) {
        return compare(o1.getSendTime(), o2.getSendTime());
    }
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }
}
