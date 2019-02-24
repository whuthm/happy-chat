package com.whuthm.happychat.ui;

import java.util.Comparator;

public class ConversationItemComparator implements Comparator<ConversationItem> {
    @Override
    public int compare(ConversationItem o1, ConversationItem o2) {
        if ((o1.isTop() && o2.isTop()) || (!o1.isTop() && o2.isTop())) {
            return (o1.getTime() < o2.getTime()) ? 1 : ((o1.getTime() == o2.getTime()) ? 0 : -1);
        } else if (o1.isTop()){
            return 1;
        } else {
            return -1;
        }
    }
}
