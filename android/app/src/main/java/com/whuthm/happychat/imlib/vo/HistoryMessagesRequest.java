package com.whuthm.happychat.imlib.vo;

public class HistoryMessagesRequest {

    private final String conversationId;
    private final long baseMessageId;
    private final boolean backward;
    private final int count;

    private HistoryMessagesRequest(Builder builder) {
        this.conversationId = builder.conversationId;
        this.baseMessageId = builder.baseMessageId;
        this.backward = builder.backward;
        this.count = builder.count;
    }

    public String getConversationId() {
        return conversationId;
    }

    public long getBaseMessageId() {
        return baseMessageId;
    }

    public boolean isBackward() {
        return backward;
    }

    public int getCount() {
        return count;
    }


    public static class Builder {
        private String conversationId;
        private long baseMessageId;
        private boolean backward = true;
        private int count;

        public void setConversationId(String conversationId) {
            this.conversationId = conversationId;
        }

        public void setBaseMessageId(long baseMessageId) {
            this.baseMessageId = baseMessageId;
        }

        public void setBackward(boolean backward) {
            this.backward = backward;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
