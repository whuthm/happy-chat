package com.whuthm.happychat.ui;

import com.whuthm.happychat.imlib.model.Message;

import java.util.Comparator;

public class MessageComparator implements Comparator<Message> {

    private final boolean ascending;

    public MessageComparator(boolean ascending) {
        this.ascending = ascending;
    }

    public MessageComparator() {
        this(true);
    }

    @Override
    public int compare(Message o1, Message o2) {
        if (ascending) {
            return compare(o1.getId(), o2.getId());
        } else {
            return compare(o2.getId(), o1.getId());
        }
    }

    public static int compare(Long x, Long y) {
        if (x == null && y != null) {
            return -1;
        } else if (x != null && y == null) {
            return 1;
        } else if (x == null) {
            return 0;
        } else {
            return x.compareTo(y);
        }
    }
}
