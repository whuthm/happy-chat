package com.whuthm.happychat.imlib.vo;

import com.whuthm.happychat.util.StringUtils;

public class HistoryMessagesRequest {

    private final String conversationId;
    private final long baseMessageId;
    private final int count;
    private final boolean ascending;
    private final LoadDataDirection loadDataDirection;

    private HistoryMessagesRequest(Builder builder) {
        this.conversationId = builder.conversationId;
        this.baseMessageId = builder.baseMessageId;
        this.count = builder.count;
        this.ascending = builder.ascending;
        this.loadDataDirection = builder.loadDataDirection;
    }

    public String getConversationId() {
        return conversationId;
    }

    public long getBaseMessageId() {
        return baseMessageId;
    }

    public boolean isAscending() {
        return ascending;
    }

    public boolean isBackward() {
        return loadDataDirection == LoadDataDirection.Backward;
    }

    public int getCount() {
        return count;
    }

    public static class Builder {
        private String conversationId;
        private long baseMessageId;
        private boolean ascending = true;
        private int count;
        private  LoadDataDirection loadDataDirection = LoadDataDirection.Backward;

        public Builder setConversationId(String conversationId) {
            this.conversationId = conversationId;
            return this;
        }

        public Builder setBaseMessageId(long baseMessageId) {
            this.baseMessageId = baseMessageId;
            return this;
        }

        public Builder setAscending(boolean ascending) {
            this.ascending = ascending;
            return this;
        }

        public Builder setCount(int count) {
            this.count = count;
            return this;
        }

        public Builder setLoadDataDirection(LoadDataDirection loadDataDirection) {
            this.loadDataDirection = loadDataDirection;
            return this;
        }

        public HistoryMessagesRequest build() {
            if (StringUtils.isEmpty(conversationId)) {
                throw new IllegalArgumentException("conversationId is empty");
            }
            return new HistoryMessagesRequest(this);
        }
    }

}
