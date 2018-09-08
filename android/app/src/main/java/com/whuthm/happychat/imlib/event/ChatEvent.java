package com.whuthm.happychat.imlib.event;

/**
 * Created by huangming on 2017/9/16.
 */

public class ChatEvent {

    ChatEvent() {

    }

    public static class TotalUnreadCountEvent extends ChatEvent {

        private final int totalUnreadCount;

        public TotalUnreadCountEvent(int totalUnreadCount) {
            this.totalUnreadCount = totalUnreadCount;
        }


        public int getTotalUnreadCount() {
            return totalUnreadCount;
        }

    }

}
