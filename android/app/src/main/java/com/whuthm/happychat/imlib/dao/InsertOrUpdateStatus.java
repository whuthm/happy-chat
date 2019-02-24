package com.whuthm.happychat.imlib.dao;

public class InsertOrUpdateStatus {

    private final boolean inserted;
    private final boolean updated;

    public InsertOrUpdateStatus(boolean inserted, boolean updated) {
        this.inserted = inserted;
        this.updated = updated;
    }

    public boolean isInserted() {
        return inserted;
    }

    public boolean isUpdated() {
        return updated;
    }
}
